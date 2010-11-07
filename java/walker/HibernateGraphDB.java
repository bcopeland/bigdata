package walker;

import org.hibernate.*;
import java.util.*;

/**
 *  This class represents an opaque view of the graph database
 *  needed by the page rank code.
 */
public class HibernateGraphDB extends BaseDB
        implements GraphDB
{
    public Object begin()
    {
        return getCurrentSession().beginTransaction();
    }

    public void commit(Object handle)
    {
        ((Transaction) handle).commit();
    }

    /* Graph table operations */
    public List<Long> getNodeIds()
    {
        return CollectionUtils.cast(getCurrentSession()
            .createQuery("select source from Edge")
            .setMaxResults(200000)
            .list());
    }

    public List<Long> getRandomNodeIds(int count)
    {
        return CollectionUtils.cast(getCurrentSession()
            .createQuery("select source from Edge group by source order by random()")
            .setMaxResults(count)
            .list());
    }

    public Long getRandomNeighbor(long source)
    {
        List<Long> list = CollectionUtils.cast(getCurrentSession()
            .createQuery("select target from Edge where source=? " + 
                         "order by random()")
            .setLong(0, source)
            .setMaxResults(1)
            .list());

        return CollectionUtils.first(list);
    }

    public List<Long> getNeighbors(long source)
    {
        return CollectionUtils.cast(getCurrentSession()
            .createQuery("select target from Edge where source=?")
            .setLong(0, source)
            .list());
    }

    /* Walk table operations */
    public long generateWalkId()
    {
        return ((Number) getCurrentSession()
            .createSQLQuery("select nextval('seq_walk_id')")
            .uniqueResult())
            .longValue();
    }

    public Map<Long,Float> getWalkCounts(String category)
    {
        List<Object[]> items = CollectionUtils.cast(getCurrentSession()
            .createSQLQuery("select source_id, count(*) " +
                "from walk " +
                "where category = ? group by source_id")
            .setString(0, category)
            .list());

        Map<Long,Float> map = new HashMap<Long,Float>();
        for (Object[] o : items)
            map.put(((Number) o[0]).longValue(), ((Number) o[1]).floatValue());

        return map;
    }

    public void saveSegment(WalkSegment ws)
    {
        getCurrentSession().save(ws);
    }
}
