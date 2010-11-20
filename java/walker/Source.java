package walker;

/**
 * An interface for stream sources.  We can attach any
 * Sink to a source.  Internally, sources are expected to
 * use an MT queue for dispatch (extending AbstractSource is
 * an easy way to do this).
 */
public interface Source<T>
{
    public void attach(Sink<T> sink);
}
