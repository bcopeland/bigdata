package walker;

import twitter4j.*;
import java.util.*;
import org.apache.log4j.*;

public class RandWalk
    implements Sink<Status>
{
    public static final float epsilon = .15f;
    public static final int numthreads = 4;
    private Logger logger = Logger.getLogger(RandWalk.class);
    private Random generator;
    private GraphDB db;

    public RandWalk(GraphDB db)
    {
        this.db = db;
        this.generator = new Random();
    }

    public void onItem(Status tweet)
    {
        for (String category : tweet.getHashtags())
            doNWalks(1, category);
    }

    public void doNWalks(int count, String category)
    {
        /* pick R random ids and run walks on all of them */
        List<Long> ids = db.getRandomNodeIds(count, category);
        int listlen = (ids.size() + numthreads - 1) / numthreads;

        Thread[] threads = new Thread[numthreads];
        for (int i=0; i < numthreads; i++)
        {
            int start = i * listlen;
            int end = Math.min(start + listlen, ids.size());
            if (end <= start)
                break;

            final GraphDB mydb = db;
            final List<Long> sublist = ids.subList(start, end);
            final float myepsilon = epsilon;
            final String mycat = category;

            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for (long id : sublist)
                        doWalk(id, mycat);
                }});

            threads[i].start();
        }

        for (int i=0; i < numthreads; i++)
        {
            try {
                if (threads[i] != null)
                    threads[i].join();
            } catch (InterruptedException ignore) {}
        }
    }

    public void doWalk(long start, String category)
    {
        long now = System.currentTimeMillis();
        Object tx = db.begin();

        long walkid = db.generateWalkId();
        int steps = doWalk(walkid, start, epsilon, category);
        long end = System.currentTimeMillis();

        db.commit(tx);
        System.out.println("walk " + start + " elapsed: " +
            (end - now) + " " + steps + " steps "); 
    }

    private int doWalk(long walkid, long start, float epsilon,
                        String category)
    {
        int step = 0;
        while (true)
        {
            float alpha = generator.nextFloat();

            if (alpha < epsilon)
                break;

            //Long nextNode = db.getRandomNeighbor(start);
            Long nextNode = getRandomNeighbor(start);
            if (nextNode == null)
                break;

            addSegment(walkid, start, nextNode, category, step);
            step++;
            start = nextNode;
        }
        return step;
    }

    public void updateWalks(Edge edge, boolean isadd)
    {
        long deleteNode = edge.getTarget();
        if (isadd)
        {
            // the probability that we should have taken a path
            // from u->v is one minus the probability p(x) that we
            // only took paths u->s s.t. s != v, which is
            // ((d(u)-1)/u)^{nwalks}.  Thus if we flip a coin
            // >= (1-p(x)), we don't have to do the update.
            long source = edge.getSource();

            List<Long> neighbors = db.getNeighbors(source);
            float nwalks = db.getWalkCount(source);
            float count = (float) neighbors.size();

            float pofx = (float) Math.pow((count-1f)/(count), nwalks);
            if (generator.nextFloat() >= 1 - pofx)
            {
                logger.debug("skipped walk update " + pofx);
                return;
            }

            logger.debug("walk update " + pofx);
            deleteNode = source;
        }

        // delete all walks touching node (by just setting a removal
        // timestamp) and run replacement walks
        Map<String, Integer> removed = db.removeWalks(deleteNode);
        for (Map.Entry<String,Integer> walk: removed.entrySet())
        {
            int count = walk.getValue();
            String category = walk.getKey();
            doNWalks(count, category);
        }
    }

    public Long getRandomNeighbor(long source)
    {
        List<Long> neighbors = db.getNeighbors(source);
        if (neighbors.isEmpty())
            return null;

        int index = generator.nextInt(neighbors.size());
        return neighbors.get(index);
    }

    private void addSegment(long walkid, long start, long next,
                            String category, int step)
    {
        if (start == next)
            return;

        WalkSegment ws =
            new WalkSegment(walkid, new Edge(start,next), category, step);

        db.saveSegment(ws);
    }

    /**
     *  Returns pagerank, normalized by all the components that
     *  have some non-zero rank.  This tends to boost rankings
     *  since there may be many zero-valued nodes, but we mostly
     *  care about the relative ranking instead.  It also means
     *  we don't need to count the number of walks, or use N and
     *  epsilon in the calculation.
     */
    public Map<Long, Float> computeRanks(String category,
        Date start, Date end)
    {
        Map<Long, Float> counts = db.getWalkCounts(category, start, end);
        float sum = 0;

        for (float value : counts.values())
            sum += value;

        if (sum == 0)
            sum = 1;

        for (Map.Entry<Long, Float> e : counts.entrySet())
            e.setValue(e.getValue() / sum);

        return counts;
    }

    public static void main(String args[])
    {
        int R = 200000;

        GraphDB db;

        int arg = 0;
        db = new HibernateGraphDB();

        String category = args[arg];
        new RandWalk(db).doNWalks(R, category);
    }
}
