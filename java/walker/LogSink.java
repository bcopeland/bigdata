package walker;

import java.util.*;

import org.apache.log4j.*;

public class LogSink<T>
    implements Sink<T>
{
    private Logger logger;

    public LogSink(String category)
    {
        logger = logger.getLogger(category);
    }

    public void onItem(T item)
    {
        logger.info(item);
    }
}
