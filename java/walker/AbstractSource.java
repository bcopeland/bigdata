package walker;

/**
 * An abstract base class for stream sources.
 */
public abstract class AbstractSource<T>
{
    protected ItemQueue<T> queue;

    public AbstractSource()
    {
        queue = new ItemQueue<T>();
    }

    public void attach(Sink<T> sink)
    {
        queue.attach(sink);
    }

    protected void produceItem(T obj)
    {
        queue.add(obj);
    }
}
