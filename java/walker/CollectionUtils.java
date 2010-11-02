package walker;

import java.util.*;

/**
 *  Utility methods on collections that aren't already supplied by 
 *  java.util.Collections.
 */
public class CollectionUtils
{
    /**
     *  Cast a list of unspecified type to a generic type, to avoid lots of
     *  SuppressWarnings annotations.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> cast(List<?> list)
    {
        return (List<T>) list;
    }

    /**
     *  Get the first item in a list.  If there is no such item, return
     *  null.
     */
    public static <T> T first(Collection<T> list)
    {
        if (list == null || list.isEmpty())
            return null;

        return list.iterator().next();
    }
}
