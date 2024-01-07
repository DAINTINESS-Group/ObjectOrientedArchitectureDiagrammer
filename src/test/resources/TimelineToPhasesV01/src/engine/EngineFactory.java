package engine;

/**
 * <h1>EngineFactory</h1>
 * Factory Class responsible for creating different types of engine objects.
 *
 * @version 1.0
 * @since 2017-07-22
 */
public final class EngineFactory
{

    /**
     * Creates a MainEngine object
     *
     * @return MainEngine object
     */
    public IEngine createMainEngine()
    {
        return new MainEngine();
    }
}
