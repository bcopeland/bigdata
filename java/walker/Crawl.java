package walker;

import twitter4j.*;
import java.util.*;

public class Crawl
    implements Sink<GraphUpdate>
{
    private GraphDB db;
    private Twitter twitter;

    public Crawl(GraphDB db)
    {
        this.db = db;
        this.twitter = new TwitterFactory().getInstance();
    }

    public void onItem(GraphUpdate item)
    {
        try
        {
            update(item.getId());
        }
        catch (TwitterException e)
        {
            e.printStackTrace();
        }
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
            System.out.println("added edge " + edge);
        }
        db.commit(tx);
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
