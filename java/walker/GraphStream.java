package walker;

import twitter4j.*;
import java.util.*;
import org.apache.log4j.*;

public class GraphStream
    extends AbstractSource<GraphUpdate>
    implements Sink<Status>
{
    private GraphDB db;
    private Twitter twitter;
    private Logger logger = Logger.getLogger(GraphStream.class);

    public GraphStream(GraphDB db)
    {
        this.db = db;
        this.twitter = new TwitterFactory().getInstance();
    }

    public void onItem(Status status)
    {
        long id = (long) status.getUser().getId();

        // we're ok?
        if (db.getOutDegree(id) == status.getUser().getFriendsCount())
            return;

        try
        {
            for (GraphUpdate item : getUpdates(id))
                produceItem(item);
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
    }

    private List<GraphUpdate> getUpdates(long source)
        throws TwitterException
    {
        List<GraphUpdate> updates = new ArrayList<GraphUpdate>();

        // get neighbor list from twitter
        int[] ids = twitter.getFriendsIDs((int) source).getIDs();
        List<Long> twitterList = new ArrayList<Long>(ids.length);
        for (int i : ids)
            twitterList.add((long) i);


        // get corresponding one from DB
        List<Long> neighbors = db.getNeighbors(source);

        List<Long> toadd = new ArrayList<Long>(twitterList);
        List<Long> todel = new ArrayList<Long>(neighbors);

        // find new ones
        toadd.removeAll(neighbors);

        // find deleted ones
        todel.removeAll(twitterList);

        for (Long id : toadd)
        {
            updates.add(new GraphUpdate(GraphUpdate.UpdateType.ADD,
                new Edge(source, id)));
        }
        for (Long id : todel)
        {
            updates.add(new GraphUpdate(GraphUpdate.UpdateType.DELETE,
                new Edge(source, id)));
        }
        return updates;
    }
}
