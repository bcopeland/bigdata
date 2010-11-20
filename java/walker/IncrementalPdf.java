package walker;

import twitter4j.*;
import java.util.*;

public class IncrementalPdf
    implements Sink<Status>
{
    private GraphDB db;
    private Twitter twitter;

    public IncrementalPdf(GraphDB db)
    {
        this.db = db;
    }

    public void onItem(Status tweet)
    {
        long id = (long) tweet.getUser().getId();
        Object tx = db.begin();
        for (String category : tweet.getHashtags())
            db.updatePdf(id, category);

        db.commit(tx);
    }
}
