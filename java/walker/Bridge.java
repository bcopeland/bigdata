package walker;

/**
 *  Bridge is a sink/source pair that simply decouples processing
 *  in onItem() so that slower processors can work out of their
 *  own queue without holding up others.
 */
public class Bridge<T> extends AbstractSource<T>
    implements Sink<T>
{
    public void onItem(T item)
    {
        produceItem(item);
    }
}
