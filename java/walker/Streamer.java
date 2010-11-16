package walker;

public class Streamer implements StreamHandler
{
    private Crawl crawl;

    public Streamer(GraphDB db)
    {
        crawl = new Crawl(db);
    }

    public void onItem(String identifier, Object object)
    {
        if (object instanceof GraphUpdate)
        {
            try {
                crawl.update(((GraphUpdate) object).getId());
            } catch (Exception e) {
                System.err.println(e);
            }
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

