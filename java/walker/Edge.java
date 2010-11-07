package walker;

import java.io.*;

public class Edge implements Serializable
{
    private long source;
    private long target;

    public Edge()
    {
    }

    public Edge(long source, long target)
    {
        this.source = source;
        this.target = target;
    }

    public void setSource(long source)
    {
        this.source = source;
    }

    public long getSource()
    {
        return source;
    }

    public void setTarget(long target)
    {
        this.target = target;
    }

    public long getTarget()
    {
        return target;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Edge))
            return false;

        Edge e = (Edge) other;
        return source == e.source && target == e.target;
    }

    public int hashCode()
    {
        return (int) (source ^ target);
    }

    public String toString()
    {
        return source + " -> " + target;
    }
}
