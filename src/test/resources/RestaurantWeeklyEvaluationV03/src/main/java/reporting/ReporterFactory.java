package reporting;

/**
 * <h1>ReporterFactory</h1>
 * Factory class responsible for creating the different types of reporting objects.
 *
 * @version 1.0
 * @since 2017-07-22
 */
public final class ReporterFactory
{

    /**
     * Creates different types of reporting objects
     *
     * @param mode: type of reporting object for creation
     * @return an object responsible for generating a report
     */
    public IReportGenerator createReporter(String mode)
    {
        if (mode.equals("ReportGeneratorForFiles"))
        {
            return new ReportGeneratorForFiles();
        }
        else if (mode.equals("ReportGeneratorForHTML"))
        {
            return new ReportGeneratorForHTML();
        }
        System.out.println("If the code got up to here, you passed a wrong argument to the ReporterFactory Factory");
        return null;
    }
}
