package walker;

import java.util.*;

public class Pair<T,U> implements Map.Entry<T,U>
{
    private T left;
    private U right;

    public Pair(T left, U right)
    {
        this.left = left;
        this.right = right;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Pair))
            return false;

        Pair<T,U> pair = (Pair<T,U>) obj;

        return left.equals(pair.left) &&
               right.equals(pair.right);
    }

    public int hashCode()
    {
        return left.hashCode();
    }

    public String toString()
    {
        return "<" + left + " " + right + ">";
    }

    public T getKey()
    {
        return left;
    }

    public U getValue()
    {
        return right;
    }

    public U setValue(U item)
    {
	this.right = item;
	return item;
    }
}
