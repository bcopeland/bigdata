package walker;

import java.io.*;

public class User implements Serializable
{
    private long source;
    private String name;

    public User()
    {
    }

    public User(long source, String name)
    {
        this.source = source;
        this.name = name;
    }

    public void setSource(long source)
    {
        this.source = source;
    }

    public long getSource()
    {
        return source;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof User))
            return false;

        User u = (User) obj;
        return source == u.getSource();
    }

    public int hashCode()
    {
        return Long.valueOf(source).hashCode();
    }
}
