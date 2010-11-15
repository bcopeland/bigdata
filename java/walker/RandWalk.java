package walker;

import java.util.*;

public class RandWalk
{
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
        int R = 200000;
        int numthreads = 4;
        float epsilon = 0.15f;

        GraphDB db;

        int arg = 0;
        db = new HibernateGraphDB();

        String category = args[arg];

        /* pick R random ids and run walks on all of them */
        List<Long> ids = db.getRandomNodeIds(R, category);
        int listlen = (ids.size() + numthreads - 1) / numthreads;
        for (int i=0; i < numthreads; i++)
        {
            int start = i * listlen;
            int end = Math.min(start + listlen, ids.size());

            final GraphDB mydb = db;
            final List<Long> sublist = ids.subList(start, end);
            final float myepsilon = epsilon;
            final String mycat = category;

            Thread th = new Thread(new Runnable() {
                public void run() {
                    RandWalk rw = new RandWalk(mydb);
                    for (long id : sublist)
                        rw.doWalk(id, myepsilon, mycat);
                }});

            th.start();
        }
    }
}
