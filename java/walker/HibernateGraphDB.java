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
            .createQuery("select source from Edge order by random()")
            .setMaxResults(200000)
            .list());
    }

    public List<Long> getRandomNodeIds(int max, String category)
    {
        Random random = new Random();

        if (category.equals(GraphDB.GLOBAL_CATEGORY))
        {
            return CollectionUtils.cast(getCurrentSession()
                .createQuery("select source from Edge " + 
                             "group by source order by random()")
                .setMaxResults(max)
                .list());
        }

        // we need an array list for O(1) accesses during binsearch
        List<Pdf> tmplist = CollectionUtils.cast(
            getCurrentSession()
                .createQuery("from Pdf where category = ? " +
                             "order by weight")
                .setString(0, category)
                .list());
 
        List<Pdf> distribution = new ArrayList<Pdf>(tmplist);
        tmplist = null;

        if (distribution.isEmpty())
            return new ArrayList<Long>();

        // give each entry in pdf some region of the probability mass
        int sum = 0;
        for (Pdf p : distribution)
        {
            p.setSummedWeight(sum + p.getWeight());
            sum += p.getWeight();
        }

        // distribution is sorted on area, so we can do
        // binary searches.
        List<Long> retlist = new ArrayList<Long>(max);
        for (int i=0; i < max; i++)
        {
            // flip a coin
            int area = random.nextInt(sum);

            // find it by searching
            int index = Collections.binarySearch(distribution, area);

            if (index < 0)
                index = -index - 1;
        
            // pick said item
            retlist.add(distribution.get(index).getSource());
        }
        return retlist;
    }

    public int getOutDegree(long source)
    {
        return ((Number) getCurrentSession()
            .createQuery("select count(*) from Edge where source=?")
            .setLong(0, source)
            .uniqueResult()).intValue();
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

    public Map<Long,Float> getWalkCounts(String category,
                Date start, Date end)
    {
        String query = "select source_id, count(*) " +
                "from walk ";
        String where = "where category = :category and remove_time is null ";
        String group = "group by source_id";

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("category", category);

        if (start != null) {
            where += "and insert_time >= :start ";
            params.put("start", start);
        }

        if (end != null) {
            where += "and insert_time < :end ";
            params.put("end", end);
        }

        List<Object[]> items = CollectionUtils.cast(getCurrentSession()
            .createSQLQuery(query + where + group)
            .setProperties(params)
            .list());

        Map<Long,Float> map = new HashMap<Long,Float>();
        for (Object[] o : items)
            map.put(((Number) o[0]).longValue(), ((Number) o[1]).floatValue());

        return map;
    }

    public int getWalkCount(long source)
    {
        return ((Number) getCurrentSession()
            .createQuery("select count(*) from WalkSegment w " +
                "where w.edge.source=?")
            .setLong(0, source)
            .uniqueResult()).intValue();
    }

    public void saveEdge(Edge e)
    {
        getCurrentSession().saveOrUpdate(e);
    }

    public void deleteEdge(Edge e)
    {
        getCurrentSession().delete(e);
    }

    public void saveSegment(WalkSegment ws)
    {
        getCurrentSession().save(ws);
    }

    public Map<String, Integer> removeWalks(long source)
    {
        // count the various categories that are affected
        List<Object[]> results = CollectionUtils.cast(getCurrentSession()
            .createSQLQuery("select walk_id, category, count(*) " +
                "from walk where source_id=? and remove_time is null " +
                "group by walk_id, category")
            .setLong(0, source)
            .list());

        Map<String,Integer> affected = new HashMap<String, Integer>();

        for (Object[] item : results)
        {
            String category = (String) item[1];
            Integer count = ((Number) item[2]).intValue();

            // aggregate based on category
            Integer current = affected.get(category);
            if (current == null)
                current = Integer.valueOf(0);

            affected.put(category, current + count);
        }

        // now "delete" them all
        getCurrentSession()
            .createSQLQuery(
                "update walk set remove_time=now() where walk_id in " +
                "(select walk_id where source_id=?)")
            .setLong(0, source)
            .executeUpdate();

        return affected;
    }

    public void updatePdf(long userid, String category)
    {
        Pdf pdf = (Pdf) getCurrentSession()
            .createQuery("from Pdf where source=? and category=?")
            .setLong(0, userid)
            .setString(1, category)
            .uniqueResult();

        if (pdf == null)
            pdf = new Pdf(userid, category);

        pdf.setWeight(pdf.getWeight() + 1);
        getCurrentSession().saveOrUpdate(pdf);
    }

    public User getUserById(long id)
    {
        return (User) getCurrentSession().get(User.class, id);
    }

    public void saveUser(User u)
    {
        getCurrentSession().save(u);
    }
}
