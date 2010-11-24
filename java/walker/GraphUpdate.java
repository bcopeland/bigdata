package walker;

public class GraphUpdate
{
    public enum UpdateType
    {
        ADD, DELETE
    };

    private Edge edge;
    private UpdateType type;

    public GraphUpdate(UpdateType type, Edge edge)
    {
        this.type = type;
        this.edge = edge;
    }

    public Edge getEdge()
    {
        return edge;
    }

    public UpdateType getType()
    {
        return type;
    }

    public String toString()
    {
        return ((type.equals(UpdateType.ADD)) ? "A" : "D") + "," +
            edge.getSource() + "," +
            edge.getTarget();
    }
}
