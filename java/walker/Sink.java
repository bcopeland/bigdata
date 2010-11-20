package walker;

/**
 * An interface for stream sinks.  The sink gets onItem()
 * called whenever a new item is available.  This may be
 * called from any thread but the call itself is synchronous.
 */
public interface Sink<T>
{
    public void onItem(T item);
}
