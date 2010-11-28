package walker;

/**
 *  Bridge is a sink/source pair that simply decouples processing
 *  in onItem() so that slower processors can work out of their
 *  own queue without holding up others.  We set wait time on
 *  inserts to the local queue to 0 so that loss happens on the
 *  requeue side of the bridge.
 */
public class Bridge<T> extends AbstractSource<T>
    implements Sink<T>
{
    public Bridge()
    {
        queue.setWaitTime(0);
    }

    public void onItem(T item)
    {
        produceItem(item);
    }
}
