package walker;

import java.util.*;
import java.util.concurrent.*;

/**
 * A queueing thread with a set of listeners.
 * In a separate thread we wait for new items on the queue
 * and dispatch them to the listeners.
 */
public class ItemQueue<T>
    implements Runnable
{
    private static final int BACKLOG = 1024;
    private LinkedBlockingQueue<T> queue;
    private Set<Sink<T>> sinks;

    public ItemQueue()
    {
        queue = new LinkedBlockingQueue<T>(BACKLOG);
        sinks = new HashSet<Sink<T>>();

        new Thread(this).start();
    }

    public void attach(Sink<T> sink)
    {
        sinks.add(sink);
    }

    public void add(T obj)
    {
        try {
            queue.offer(obj, 60L, TimeUnit.SECONDS);
        } 
        catch (InterruptedException ex) {}
    }

    public void run()
    {
        try
        {
            while (true)
            {
                T item = queue.take();
                for (Sink<T> sink : sinks)
                    sink.onItem(item);
            }
        }
        catch (InterruptedException ignore) {}
    }
}
