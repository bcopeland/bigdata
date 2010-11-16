package walker;

public class GraphUpdate
{
    private long id;

    public GraphUpdate(long id)
    {
        this.id = id;
    }

    public long getId()
    {
        return id;
    }

    public String toString()
    {
        return "vertex " + id + " updated";
    }
}
