package walker;

import twitter4j.*;
import java.util.*;
import org.apache.log4j.*;

public class UsernameSink
    implements Sink<Status>
{
    private GraphDB db;
    private Twitter twitter;
    private Logger logger = Logger.getLogger(UsernameSink.class);

    public UsernameSink(GraphDB db)
    {
        this.db = db;
        this.twitter = new TwitterFactory().getInstance();
    }

    public void onItem(Status tweet)
    {
        long id = (long) tweet.getUser().getId();
        User user = db.getUserById(id);
        if (user != null)
            return;

        String username = getScreenName(id);
        if (username == null)
            return;  // oh well, we tried...

        Object tx = db.begin();
        user = new User(id, username);
        db.saveUser(user);
        db.commit(tx);
    }

    private String getScreenName(long id)
    {
        String result = null;

        // try to get it from twitter...
        twitter4j.User user = null;
        try
        {
            user = twitter.showUser((int) id);
        }
        catch (TwitterException e)
        {
            if (e.exceededRateLimitation())
            {
                try {
                    int secs = e.getRateLimitStatus()
                    .getSecondsUntilReset();

                    logger.info("Rate limit: " + secs);
                    Thread.sleep(secs * 1000);
                } catch (InterruptedException ie) {}
            }
        }

        if (user != null)
            result = user.getScreenName();

        return result;
    }
}
