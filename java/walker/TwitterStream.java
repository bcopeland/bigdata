package walker;

import twitter4j.*;

public class TwitterStream
    extends AbstractSource<Status>
    implements Runnable, StatusListener
{
    private StatusStream stream;
    private Thread thread;

    public TwitterStream()
        throws TwitterException
    {
        twitter4j.TwitterStream twitter =
            new TwitterStreamFactory().getInstance();
        stream = twitter.getSampleStream();
        start();
    }

    public void start()
    {
        thread = new Thread(this);
        thread.start();
    }

    public void run()
    {
        try
        {
            while (true)
                stream.next(this);
        }
        catch (TwitterException e)
        {
            System.err.println(e);
        }
    }

    public void onStatus(Status status)
    {
        produceItem(status);
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice)
    {
    }

    public void onException(Exception e)
    {
    }

    public void onTrackLimitationNotice(int number)
    {
    }
}
