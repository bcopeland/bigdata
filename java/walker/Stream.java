package walker;

import java.util.*;

public abstract class Stream
{
    private List<StreamHandler> handlers;

    public Stream()
    {
        handlers = new ArrayList<StreamHandler>();
    }

    public void registerHandler(StreamHandler handler)
    {
        handlers.add(handler);
    }

    public void unregisterHandler(StreamHandler handler)
    {
        handlers.remove(handler);
    }

    protected void doCallback(Object item)
    {
        for (StreamHandler h : handlers)
            h.onItem(getIdentifier(), item);
    }

    public abstract String getIdentifier();

    public void start()
    {
    }
}
