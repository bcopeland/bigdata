package walker;

import org.hibernate.*;
import java.util.*;

/**
 *  This class represents an opaque view of the graph database
 *  needed by the page rank code.
 */
public interface GraphDB
{
    public static final String GLOBAL_CATEGORY = "global";

    Object begin();
    void commit(Object handle);
    List<Long> getNodeIds();
    Long getRandomNeighbor(long source);
    List<Long> getRandomNodeIds(int max, String category);
    List<Long> getNeighbors(long source);
    int getOutDegree(long source);
    long generateWalkId();
    Map<Long,Float> getWalkCounts(String category);
    void saveEdge(Edge e);
    void saveSegment(WalkSegment ws);
}
