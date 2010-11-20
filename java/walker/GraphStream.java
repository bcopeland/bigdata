package walker;

import twitter4j.*;

public class GraphStream
    extends AbstractSource<GraphUpdate>
    implements Sink<Status>
{
    private GraphDB db;

    public GraphStream(GraphDB db)
    {
        this.db = db;
    }

    public void onItem(Status status)
    {
        /* convert to graph update */
        long id = (long) status.getUser().getId();
        if (db.getOutDegree(id) != status.getUser().getFriendsCount())
        {
            produceItem(new GraphUpdate(id));
        }
    }
}
