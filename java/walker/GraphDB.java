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
    public Long getRandomNeighbor(long source);
    public List<Long> getRandomNodeIds(int count);
    public List<Long> getNeighbors(long source);
    long generateWalkId();
    Map<Long,Float> getWalkCounts(String category);
    void saveEdge(Edge e);
    void saveSegment(WalkSegment ws);
}
