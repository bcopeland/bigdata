package walker;

import twitter4j.*;

public class Streamer implements StreamHandler
{
    private Crawl crawl;
    private IncrementalPdf pdf;

    public Streamer(GraphDB db)
    {
        crawl = new Crawl(db);
        pdf = new IncrementalPdf(db);
    }

    public void onItem(String identifier, Object object)
    {
        if (object instanceof GraphUpdate)
        {
            try {
                crawl.update(((GraphUpdate) object).getId());
            } catch (TwitterException e) {
                if (e.exceededRateLimitation())
                {
                    try {
                        int secs = e.getRateLimitStatus()
                            .getSecondsUntilReset();

                        System.out.println("RL sleeping " + secs);
                        Thread.sleep(secs * 1000);
                    } catch (InterruptedException ie) {}
                }
                else
                    System.err.println(e);
            }
        }
        else if (object instanceof Status)
        {
            pdf.update((Status) object);
        }
        System.out.println("item: " + object);
    } 

    public static void main(String[] args)
        throws Exception
    {
        GraphDB db = new HibernateGraphDB();
        TwitterStream ts = new TwitterStream();
        GraphStream gs = new GraphStream(ts, db);

        Streamer streamer = new Streamer(db);

        gs.registerHandler(streamer);
        ts.registerHandler(streamer);
        ts.start();
        gs.start();
    }
}

