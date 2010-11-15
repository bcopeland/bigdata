package walker;

import twitter4j.*;

public class TwitterStream extends Stream
    implements Runnable, StatusListener
{
    private StatusStream stream;

    public TwitterStream()
        throws TwitterException
    {
        twitter4j.TwitterStream twitter =
            new TwitterStreamFactory().getInstance();
        stream = twitter.getSampleStream();
    }

    public void start()
    {
        new Thread(this).start();
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
        doCallback(status);
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

    public String getIdentifier()
    {
        return "twitter";
    }
}
