package walker;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.*;
import org.neo4j.kernel.*;
import java.util.*;

/**
 *  This class represents an opaque view of the graph database
 *  needed by the page rank code.
 */
public class Neo4JGraphDB
    implements GraphDB
{
    enum MyRelationshipType implements RelationshipType {
        FOLLOWS, WALKS_TO 
    }

    private GraphDatabaseService graphDb;
    private Random idgen = new Random();
    private RelationshipIndex walks;
    private Index<Node> names;

    private static final String ID_KEY = "id";
    private static final String CATEGORY = "category";
    private static final String ORDINAL = "ordinal";

    public Neo4JGraphDB()
    {
        graphDb = new EmbeddedGraphDatabase("bdslss");
        names = graphDb.index().forNodes("names");
        walks = graphDb.index().forRelationships("walks");

        Runtime.getRuntime().addShutdownHook(
            new Thread() {
	       public void run()
	       {
	            graphDb.shutdown();
	       }});
    }

    public Object begin()
    {
        return graphDb.beginTx();
    }

    public void commit(Object handle)
    {
        ((Transaction) handle).success();
    }

    /* Graph table operations */

    // FIXME this should use paging or something.
    public List<Long> getNodeIds()
    {
        List<Long> l = new ArrayList<Long>();
        int count = 0;
        for (Node n : graphDb.getAllNodes()) {
            try {
                l.add((Long) n.getProperty(ID_KEY));
                if (count++ > 20000)
                    break;
            }
            catch (Exception e) {}
        }

        return l;
    }

    public List<Long> getRandomNodeIds(int count)
    {
        List<Long> l = getNodeIds();
        Collections.shuffle(l);
        return l;
    }

    public Long getRandomNeighbor(long source)
    {
        List<Long> neighbors = getNeighbors(source);
        if (neighbors.isEmpty())
            return null;

        int index = idgen.nextInt(neighbors.size());
        return neighbors.get(index);
    }


    public List<Long> getNeighbors(long source)
    {
        List<Long> l = new ArrayList<Long>();
        Node n = getOrCreateNode(source);
        for (Relationship r : n.getRelationships(MyRelationshipType.FOLLOWS))
            l.add((Long) r.getEndNode().getProperty(ID_KEY));

        return l;
    }

    /* Walk table operations */
    public long generateWalkId()
    {
        // This is pretty cheesy, but will work with possible collisions for now.
        return idgen.nextLong();
    }

    public Map<Long,Float> getWalkCounts(String category)
    {
        // We just need to iterate over all 'category' walks and
        // count the degree for each source id.
        Map<Long,Float> map = new HashMap<Long,Float>();
        for (Relationship r : walks.get(CATEGORY, category))
        {
            long id = (Long) r.getStartNode().getProperty(ID_KEY);
            float fval = 0;
            Float fl = map.get(id);
            if (fl != null)
                fval = fl.floatValue();
            
            map.put(id, fl + 1);
        }
        return map;
    }

    private Node getOrCreateNode(long id)
    {
        Node node = names.get(ID_KEY, id).getSingle();
        if (node != null)
            return node;

        node = graphDb.createNode();
        node.setProperty(ID_KEY, id);
        names.add(node, ID_KEY, id);
        return node;
    }

    public void saveSegment(WalkSegment ws)
    {
        // get both nodes
        Node n1 = getOrCreateNode(ws.getEdge().getSource());
        Node n2 = getOrCreateNode(ws.getEdge().getTarget());

        Relationship r1 = n1.createRelationshipTo(n2, MyRelationshipType.WALKS_TO);
        r1.setProperty(ID_KEY, ws.getWalkid());
        r1.setProperty(CATEGORY, ws.getCategory());
        r1.setProperty(ORDINAL, ws.getOrdinal());

        // now update whatever indexes we need
        walks.add(r1, CATEGORY, ws.getCategory());
    }
}
