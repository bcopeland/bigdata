package walker;

public class Streamer implements StreamHandler
{
    public Streamer()
    {
    }

    public void onItem(String identifier, Object object)
    {
        System.out.println("item: " + object);
    } 

    public static void main(String[] args)
        throws Exception
    {
        TwitterStream ts = new TwitterStream();
        GraphStream gs = new GraphStream(ts, new HibernateGraphDB());

        Streamer streamer = new Streamer();

        gs.registerHandler(streamer);
        ts.registerHandler(streamer);
        ts.start();
        gs.start();
    }
}

