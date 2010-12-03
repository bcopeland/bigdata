package walker;

import java.util.*;

import twitter4j.*;

/**
 *  Continually print top 20 rankings
 */
public class Top20
{
    public static final int LINES = 23;
    public static final int CACHE_SIZE = 5000;
    private GraphDB db;
    private Map<Long,String> nameCache;
    private Twitter twitter;
    private RandWalk randWalk;

    public Top20(GraphDB db)
    {
        this.db = db;
        randWalk = new RandWalk(db);
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
        }

        // still no dice?  try to get it from twitter...
        if (result == null)
        {
            twitter4j.User user = null;
            try
            {
                user = twitter.showUser((int) id);
            } catch (TwitterException ignore) {}

            if (user == null)
                // don't have it anywhere -- don't cache it
                return String.valueOf(id);

            result = user.getScreenName();

            // save the username
            User newUser = new User(id, result);
            Object tx = db.begin();
            db.saveUser(newUser);
            db.commit(tx);
        }
        nameCache.put(id, result); 
        return result;
    }

    public List<Map.Entry<String, Float>> getTopK(int n, String category, Date start, Date end)
    {
        /* print out the page rank */
        List<Map.Entry<Long, Float>> elements =
            new ArrayList<Map.Entry<Long, Float>>(
                randWalk.computeRanks(category, start, end).entrySet());
        

    
        Collections.sort(elements,
            new Comparator<Map.Entry<Long,Float>>() {
                public int compare(Map.Entry<Long,Float> o1,
                                   Map.Entry<Long,Float> o2)
                {
                    return o2.getValue().compareTo(o1.getValue());
                }});

        int i = 0;
        if (n == -1)
            n = elements.size();

        List<Map.Entry<String, Float>> results =
            new ArrayList<Map.Entry<String, Float>>(n);

        for (Map.Entry<Long,Float> x : elements)
        {
            results.add(new Pair<String,Float>(
                resolve(x.getKey()), x.getValue()));
            if (++i >= n)
                break;
        }
        return results;
    }

    public void go(int count, String category)
    {
        String CLR = "\u001b[2J";

        while (true)
        {
            int i = 0;
            List<Map.Entry<String, Float>> elements =
                getTopK(count, category, null, null);

            System.out.print(CLR);
            for (Map.Entry<String,Float> x : elements)
            {
                System.out.printf("%30s\t\t\t%.10f\n", x.getKey(),
                    x.getValue());
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

        new Top20(db).go(20, category);
    }
}
