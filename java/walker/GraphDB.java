package walker;

import org.hibernate.*;
import java.util.*;

/**
 *  This class represents an opaque view of the graph database
 *  needed by the page rank code.
 */
public interface GraphDB
{
    Object begin();
    void commit(Object handle);
    List<Long> getNodeIds();
    List<Long> getNeighbors(long source);
    long generateWalkId();
    Map<Long,Float> getWalkCounts(String category);
    void saveSegment(WalkSegment ws);
}
