package walker;

import twitter4j.*;

public class GraphStream extends Stream
    implements StreamHandler
{
    private walker.TwitterStream stream;
    private GraphDB db;

    public GraphStream(TwitterStream stream, GraphDB db)
        throws TwitterException
    {
        this.stream = stream;
        this.db = db;
    }

    public void start()
    {
        stream.registerHandler(this);
    }

    public void onItem(String stream, Object object)
    {
        Status status = (Status) object;

        /* convert to graph update */
        long id = (long) status.getUser().getId();
        if (db.getOutDegree(id) != status.getUser().getFriendsCount())
        {
            doCallback(new GraphUpdate(id));
        }
    }

    public String getIdentifier()
    {
        return "graph";
    }
}
