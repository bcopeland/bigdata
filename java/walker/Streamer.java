package walker;

import twitter4j.*;
import java.io.*;

public class Streamer
{
    public Streamer(GraphDB db, Source<Status> ts)
        throws TwitterException
    {
        // create the streams

        GraphStream gs = new GraphStream(db);

        // create sinks
        Sink<Status> pdf = new IncrementalPdf(db);
        Sink<Status> walker = new RandWalk(db);
        Sink<GraphUpdate> graphSink = new GraphUpdateSink(db);

        Sink<Status> statuslog = new LogSink<Status>("status");
        Sink<GraphUpdate> graphlog = new LogSink<GraphUpdate>("graph");

        Bridge<Status> bridge = new Bridge<Status>();
        Bridge<Status> walkBridge = new Bridge<Status>();

        // hook up the sinks to sources
        ts.attach(pdf);
        ts.attach(statuslog);
        ts.attach(bridge);

        walkBridge.attach(walker);

        bridge.attach(gs);
        gs.attach(graphSink);
        gs.attach(graphlog);

        // now wait for the other threads to end... 
    }

    public static void main(String[] args)
        throws Exception
    {
        Source<Status> ts = null;

        if (args.length > 0)
        {
            if (args[0].equals("-t"))
            {
                ts = new TrackStatusSource();
            }
            else 
            {
                InputStream is = new BufferedInputStream(
                    new FileInputStream(args[0]));
                ts = new TwitterStream(is);
            }
        }
        if (ts == null)
            ts = new TwitterStream();

        new Streamer(new HibernateGraphDB(), ts);
    }
}

