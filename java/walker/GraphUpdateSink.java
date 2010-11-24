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
        // something like:
        // if delete then delete all walks touching node
        //   (by just setting a removal timestamp?)
        //   and run that many walks
        //
        // if add then compute prob
        //   1 - (1-1/d(source))^w(source)
        // if rand() > prob then do the above
        // should go in randWalk() ?
    }
}
