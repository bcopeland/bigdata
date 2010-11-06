package walker;

import java.util.*;

public class PrintRank
{
    public static void main(String args[])
    {
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

        /* print out the page rank */
        for (Map.Entry<Long, Float> e :
                rw.computeRanks(category).entrySet())
            System.out.println(e.getKey() + "\t" + e.getValue());
    }
}
