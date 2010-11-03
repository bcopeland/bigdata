package walker;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.*;
import org.neo4j.kernel.*;
import java.util.*;
import java.io.*;

public class Neo4JBulkLoad
{
    enum MyRelationshipType implements RelationshipType {
        FOLLOWS, WALKS_TO 
    }

    private GraphDatabaseService graphDb;
    private Index<Node> names;
    private Map<Long,Node> idmap = new TreeMap<Long,Node>();

    private static final String ID_KEY = "id";

    public Neo4JBulkLoad()
    {
        graphDb = new EmbeddedGraphDatabase("bdslss");
        names = graphDb.index().forNodes("names");

        Runtime.getRuntime().addShutdownHook(
            new Thread() {
	       public void run()
	       {
	            graphDb.shutdown();
	       }});
    }

    public void load(String filename)
        throws Exception
    {
        int count = 0;
        BufferedReader r = new BufferedReader(new FileReader(filename));

        String line;
        Transaction tx = graphDb.beginTx();
        while ((line = r.readLine()) != null)
        {
            String[] vals = line.split("\\|");
            long id1 = Long.parseLong(vals[0]);
            long id2 = Long.parseLong(vals[3]);
            Node node1 = getOrCreateNode(id1);
            Node node2 = getOrCreateNode(id2);

            node1.createRelationshipTo(node2, MyRelationshipType.FOLLOWS);
            if ((count++ % 100000) == 0) {
                System.out.print(".");
                tx.success();
                tx.finish();
                tx = graphDb.beginTx();
            }
        }
        tx.success();
        tx.finish();
    }

    private Node getOrCreateNode(long id)
    {
        Node node = idmap.get(id);
        if (node != null)
            return node;

        node = graphDb.createNode();
        node.setProperty(ID_KEY, id);
        names.add(node, ID_KEY, id);
        idmap.put(id, node);
        return node;
    }

    public static void main(String args[])
        throws Exception
    {
        new Neo4JBulkLoad().load(args[0]);
    }
}
