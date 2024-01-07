package analysis;

/**
 * <h1>AnalyserFactory</h1>
 * Factory class responsible for creating different types of analyser objects
 *
 * @version 1.0
 * @since 2017-07-23
 */
public final class AnalyserFactory
{
    /**
     * Creates different types of analyser objects
     *
     * @param concreteClassName analyser type
     * @return an IAnalyser to be used by MainEngine
     */
    public IAnalyser createAnalyser(String concreteClassName)
    {
        if (concreteClassName.equals("NaiveAnalyser"))
        {
            return new NaiveAnalyser();
        }
        System.out.println("If the code got up to here, you passed a wrong argument to AnalyserFactory");
        return null;
    }
}
