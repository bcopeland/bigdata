package walker;

import twitter4j.*;
import java.util.*;

import org.apache.log4j.*;

public class GraphUpdateSink
    implements Sink<GraphUpdate>
{
    private static final Logger logger =
        Logger.getLogger(GraphUpdateSink.class);

    private GraphDB db;
    private Twitter twitter;
    private RandWalk randWalk;

    public GraphUpdateSink(GraphDB db)
    {
        this.db = db;
        randWalk = new RandWalk(db);
    }

    public void onItem(GraphUpdate item)
    {
        Edge edge = item.getEdge();

        Object tx = db.begin();
        if (item.getType().equals(GraphUpdate.UpdateType.ADD))
            db.saveEdge(edge);
        else
            db.deleteEdge(edge);
        db.commit(tx);

        doWalkUpdates(item);
    }

    public void doWalkUpdates(GraphUpdate item)
    {
        randWalk.updateWalks(item.getEdge(),
            item.getType().equals(GraphUpdate.UpdateType.ADD));
    }
}
