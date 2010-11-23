package walker;

import twitter4j.*;
import java.io.*;

public class Streamer
{
    public Streamer(GraphDB db, InputStream input)
        throws TwitterException
    {
        // create the streams
        TwitterStream ts;

        if (input == null)
            ts = new TwitterStream();
        else
            ts = new TwitterStream(input);

        GraphStream gs = new GraphStream(db);

        // create sinks
        Sink<Status> pdf = new IncrementalPdf(db);
        Sink<Status> users = new UsernameSink(db);
        Sink<GraphUpdate> crawl = new Crawl(db);

        Sink<Status> statuslog = new LogSink<Status>("status");
        Sink<GraphUpdate> graphlog = new LogSink<GraphUpdate>("graph");

        // hook up the sinks to sources
        ts.attach(pdf);
        ts.attach(statuslog);
        ts.attach(users);
        ts.attach(gs);

        gs.attach(crawl);
        gs.attach(graphlog);

        // now wait for the other threads to end... 
    }

    public static void main(String[] args)
        throws Exception
    {
        InputStream is = null;
        if (args.length > 0) {
            is = new BufferedInputStream(
                new FileInputStream(args[0]));
        }
        new Streamer(new HibernateGraphDB(), is);
    }
}

