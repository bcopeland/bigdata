package walker;

import java.util.*;

public class RandWalk
{
    public static final String GLOBAL_CATEGORY = "global";

    private Random generator;
    private GraphDB db;

    public RandWalk(GraphDB db)
    {
        this.db = db;
        this.generator = new Random();
    }

    public void doWalk(long start, float epsilon, String category)
    {
        long now = System.currentTimeMillis();
        Object tx = db.begin();

        long walkid = db.generateWalkId();
        doWalk(walkid, start, epsilon, category, 0);
        long end = System.currentTimeMillis();

        db.commit(tx);
        System.out.println("walk " + start + " elapsed: " +
            (end - now)); 
    }

    private void doWalk(long walkid, long start, float epsilon,
                        String category, int step)
    {
        while (true)
        {
            float alpha = generator.nextFloat();

            if (alpha < epsilon)
                break;

            List<Long> neighbors = db.getNeighbors(start);
            if (neighbors.isEmpty())
                break;

            int index = generator.nextInt(neighbors.size());
            long nextNode = neighbors.get(index);

            addSegment(walkid, start, nextNode, step);
            step++;
            start = nextNode;
        }
    }

    private void addSegment(long walkid, long start, long next, int step)
    {
        WalkSegment ws =
            new WalkSegment(walkid, new Edge(start,next), "global", step);

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
    public Map<Long, Float> computeRanks(String category)
    {
        Map<Long, Float> counts = db.getWalkCounts(category);
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
        Random random = new Random(123);
        int R = 1000;
        float epsilon = 0.15f;
        GraphDB db = new HibernateGraphDB();

        RandWalk rw = new RandWalk(db);

        /* FIXME do this in 4 threads */

        Long[] ids = db.getNodeIds().toArray(new Long[0]);

        /* pick R random ids and run walks on all of them */
        for (int i=0; i < R; i++)
        {
            long id = ids[random.nextInt(ids.length)];
            rw.doWalk(id, epsilon, RandWalk.GLOBAL_CATEGORY);
        }

        /* print out the page rank */
        for (Map.Entry<Long, Float> e :
                rw.computeRanks(RandWalk.GLOBAL_CATEGORY).entrySet())
            System.out.println(e.getKey() + ": " + e.getValue());
    }
}
