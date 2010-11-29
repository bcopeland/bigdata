package walker;

import twitter4j.*;
import java.io.*;

public class TrackStatusSource
    extends AbstractSource<Status>
    implements Runnable, StatusListener
{
    public static final String[] categories = 
    {
        "news", "jobs", "ZodiacFacts", "blackfriday",
        "music", "newtwitter", "iphone", "travel",
        "health", "love", "android", "linux", "football"
    };

    private StatusStream stream;
    private Thread thread;

    public TrackStatusSource()
        throws TwitterException
    {
        twitter4j.TwitterStream twitter =
            new TwitterStreamFactory().getInstance();
        stream = twitter.getFilterStream(new FilterQuery().track(categories));
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
