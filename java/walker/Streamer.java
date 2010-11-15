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
    {
        Stream ts = new TwitterStream();
        ts.registerHandler(new Streamer());
        ts.start();
    }
}

