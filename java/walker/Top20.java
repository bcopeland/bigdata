package walker;

import java.util.*;

/**
 *  Continually print top 20 rankings
 */
public class Top20
{
    public static void main(String args[])
    {
        String CLR = "\u001b[2J";

        int arg = 0;

        GraphDB db;

        if (args.length > 0 && args[0].equals("neo4j")) {
            db = new Neo4JGraphDB();
            arg++;
        }
        else
            db = new HibernateGraphDB();

        String category = args[arg];

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
                System.out.printf("%20d\t\t\t\t%.10f\n", x.getKey(), x.getValue());
                if (++i >= 24)
                    break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}
