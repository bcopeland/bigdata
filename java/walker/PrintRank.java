package walker;

import java.util.*;

public class PrintRank
{
    public static void main(String args[])
    {
        String category = args[0];
        GraphDB db = new GraphDB();

        RandWalk rw = new RandWalk(db);

        /* print out the page rank */
        for (Map.Entry<Long, Float> e :
                rw.computeRanks(category).entrySet())
            System.out.println(e.getKey() + ": " + e.getValue());
    }
}
