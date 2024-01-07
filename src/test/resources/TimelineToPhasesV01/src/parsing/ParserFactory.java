package parsing;

/**
 * <h1>ParserFactory</h1>
 * Factory class responsible for creating different types of parser objects which are responsible for reading the input files.
 *
 * @version 1.0
 * @since 2017-07-23
 */
public final class ParserFactory
{

    /**
     * Creates different types of parser objects which are responsible for reading the input files.
     *
     * @param concreteClassName parser type
     * @return parser object
     */
    public IParser createParser(String concreteClassName)
    {
        if (concreteClassName.equals("TestParser"))
        {
            return new TestParser();
        }
        else if (concreteClassName.equals("SimpleTextParser"))
        {
            return new SimpleTextParser();
        }

        System.out.println("If the code got up to here, you passed a wrong argument to AnalyserFactory");
        return null;
    }
}

