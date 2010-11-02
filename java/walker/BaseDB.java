package walker;

import org.hibernate.*;

/**
 *  This class represents an opaque view of the graph database
 *  needed by the page rank code.
 */
public class BaseDB
{
    public Session getCurrentSession()
    {
        return HibernateUtil.currentSession();
    }
}
