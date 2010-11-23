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

        String username = tweet.getUser().getScreenName();

        Object tx = db.begin();
        user = new User(id, username);
        db.saveUser(user);
        db.commit(tx);
    }
}
