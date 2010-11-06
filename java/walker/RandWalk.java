package walker;

import java.util.*;

public class RandWalk
{
    public static final String GLOBAL_CATEGORY = "global";

    private Random generator;
    private GraphDB db;
    private int total_degree;

    public RandWalk(GraphDB db)
    {
        this.db = db;
        this.generator = new Random();
    }

    public void doWalk(long start, float epsilon, String category)
    {
        long now = System.currentTimeMillis();
        Object tx = db.begin();

        total_degree = 0;
        long walkid = db.generateWalkId();
        int steps = doWalk(walkid, start, epsilon, category);
        long end = System.currentTimeMillis();

        db.commit(tx);
        System.out.println("walk " + start + " elapsed: " +
            (end - now) + " " + steps + " steps " + " d:" + total_degree); 
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

            addSegment(walkid, start, nextNode, step);
            step++;
            start = nextNode;
        }
        return step;
    }

    public Long getRandomNeighbor(long source)
    {
        List<Long> neighbors = db.getNeighbors(source);
        if (neighbors.isEmpty())
            return null;

        total_degree += neighbors.size();
        int index = generator.nextInt(neighbors.size());
        return neighbors.get(index);
    }

    private void addSegment(long walkid, long start, long next, int step)
    {
        if (start == next)
            return;

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

        GraphDB db;

        if (args.length > 0 && args[0].equals("neo4j"))
            db = new Neo4JGraphDB();
        else
            db = new HibernateGraphDB();

        RandWalk rw = new RandWalk(db);

        /* FIXME do this in 4 threads */

        /* pick R random ids and run walks on all of them */
        List<Long> ids = db.getRandomNodeIds(R);
        for (long id : ids)
            rw.doWalk(id, epsilon, RandWalk.GLOBAL_CATEGORY);

        /* print out the page rank */
        for (Map.Entry<Long, Float> e :
                rw.computeRanks(RandWalk.GLOBAL_CATEGORY).entrySet())
            System.out.println(e.getKey() + ": " + e.getValue());
    }
}
