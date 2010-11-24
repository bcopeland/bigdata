package walker;

import twitter4j.*;
import java.util.*;

import org.apache.log4j.*;

public class Crawl
{
    private static final Logger logger = Logger.getLogger(Crawl.class);
    private GraphDB db;
    private Twitter twitter;

    public Crawl(GraphDB db)
    {
        this.db = db;
        this.twitter = new TwitterFactory().getInstance();
    }

    public void update(long source)
        throws TwitterException
    {
        // get neighbor list from twitter
        int[] ids = twitter.getFriendsIDs((int) source).getIDs();
        List<Long> twitterList = new ArrayList<Long>(ids.length);
        for (int i : ids)
            twitterList.add((long) i);

        // get corresponding one from DB
        List<Long> neighbors = db.getNeighbors(source);

        // now add any missing ones...
        twitterList.removeAll(neighbors);
        Object tx = db.begin();
        for (long dest : twitterList) {
            Edge edge = new Edge(source, dest);
            db.saveEdge(edge);
        }
        db.commit(tx);

        // FIXME do we want to update the walks here?
    }
        
    public static void main(String args[])
        throws Exception
    {
        int arg = 0;

        GraphDB db;

        db = new HibernateGraphDB();

        long id = Long.valueOf(args[arg]);

        new Crawl(db).update(id);
    }
}
