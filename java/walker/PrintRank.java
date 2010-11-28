package walker;

import java.util.*;

public class PrintRank
{
    public static void main(String args[])
    {
        int arg = 0;

        GraphDB db = new HibernateGraphDB();

        String category = args[arg];

        RandWalk rw = new RandWalk(db);

        /* print out the page rank */
        for (Map.Entry<Long, Float> e :
                rw.computeRanks(category, null, null).entrySet())
            System.out.println(e.getKey() + "\t" + e.getValue());
    }
}
