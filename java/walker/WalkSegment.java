package walker;

import java.io.*;

public class WalkSegment implements Serializable
{
    private long walkid;
    private int ordinal;
    private Edge edge;
    private String category;

    public WalkSegment()
    {
    }

    public WalkSegment(long walkid, Edge edge, String category, int ordinal)
    {
        this.walkid = walkid;
        this.edge = edge;
        this.category = category;
        this.ordinal = ordinal;
    }

    public void setWalkid(long walkid)
    {
        this.walkid = walkid;
    }

    public long getWalkid()
    {
        return walkid;
    }

    public void setEdge(Edge edge)
    {
        this.edge = edge;
    }

    public Edge getEdge()
    {
        return edge;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }

    public void setOrdinal(int ordinal)
    {
        this.ordinal = ordinal;
    }

    public int getOrdinal()
    {
        return ordinal;
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof WalkSegment))
            return false;

        WalkSegment ws = (WalkSegment) other;
        return ordinal == ws.ordinal &&
               walkid == ws.walkid;
    }

    public int hashCode()
    {
        return ordinal ^ (int) walkid;
    }
}
