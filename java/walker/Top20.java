package walker;

import java.util.*;

import twitter4j.*;

/**
 *  Continually print top 20 rankings
 */
public class Top20
{
    public static final int LINES = 23;
    public static final int CACHE_SIZE = 1000;
    private GraphDB db;
    private Map<Long,String> nameCache;
    private String category;
    private Twitter twitter;

    public Top20(GraphDB db, String category)
    {
        this.db = db;
        this.category = category;
        nameCache = new LinkedHashMap<Long, String>() {
            protected boolean removeEldestEntry(Map.Entry<Long,String> eldest)
            {
                return size() > CACHE_SIZE;
            }};
        twitter = new TwitterFactory().getInstance();
    }

    public String resolve(long id)
    {
        String result = nameCache.get(id);

        // try to get it from DB
        if (result == null)
        {
            User user = db.getUserById(id);
            if (user != null)
                result = user.getName();
            else
                // don't cache...
                return String.valueOf(id);
        }

        // still no dice?  try to get it from twitter...
        /*
        if (result == null)
        {
            twitter4j.User user = null;
            try
            {
                user = twitter.showUser((int) id);
            } catch (TwitterException ignore) {}

            if (user != null)
                result = user.getScreenName();
            else
                result = String.valueOf(id);
        }
        */
        nameCache.put(id, result); 
        return result;
    }

    public void go()
    {
        String CLR = "\u001b[2J";

        RandWalk rw = new RandWalk(db);

        while (true)
        {
            /* print out the page rank */
            List<Map.Entry<Long, Float>> elements =
                new ArrayList<Map.Entry<Long, Float>>(
                    rw.computeRanks(category).entrySet());
    
            Collections.sort(elements,
                new Comparator<Map.Entry<Long,Float>>() {
                    public int compare(Map.Entry<Long,Float> o1,
                                       Map.Entry<Long,Float> o2)
                    {
                        return o2.getValue().compareTo(o1.getValue());
                    }});

            System.out.print(CLR);
            
            int i = 0;
            for (Map.Entry<Long,Float> x : elements)
            {
                System.out.printf("%30s\t\t\t%.10f\n", resolve(x.getKey()),
                    x.getValue());
                if (++i >= LINES)
                    break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }

    public static void main(String args[])
    {
        int arg = 0;
        GraphDB db;

        db = new HibernateGraphDB();

        String category = args[arg];

        new Top20(db, category).go();
    }
}
