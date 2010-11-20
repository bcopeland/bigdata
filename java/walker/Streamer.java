package walker;

import twitter4j.*;

public class Streamer
{
    public Streamer(GraphDB db)
        throws TwitterException
    {
        // create the streams
        TwitterStream ts = new TwitterStream();
        GraphStream gs = new GraphStream(db);

        // create sinks
        Sink<Status> pdf = new IncrementalPdf(db);
        Sink<GraphUpdate> crawl = new Crawl(db);

        Sink<Status> statuslog = new LogSink<Status>("status");
        Sink<GraphUpdate> graphlog = new LogSink<GraphUpdate>("graph");

        // hook up the sinks to sources
        ts.attach(pdf);
        ts.attach(statuslog);

        ts.attach(gs);
        gs.attach(crawl);
        gs.attach(graphlog);

        // now wait for the other threads to end... 
    }

    public static void main(String[] args)
        throws Exception
    {
        new Streamer(new HibernateGraphDB());
    }
}

