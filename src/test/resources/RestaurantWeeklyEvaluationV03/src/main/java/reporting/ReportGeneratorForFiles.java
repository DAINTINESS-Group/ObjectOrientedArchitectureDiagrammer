package reporting;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.util.List;
import java.util.Objects;

import model.Dish;
import model.Employee;

/**
 * <h1>ReportGeneratorForFiles</h1>
 * Responsible for saving the restaurant statistics in plain text format
 * <p>
 * Observe that the class has package-private visibility: clients should only use the public Interface for work-to-be-done and the factory for object creation
 * <p>
 * The class is final: cannot be subclassed; if need, create a new v. of the class and exploit the factory for its generation
 *
 * @version 1.0
 * @since 2017-07-22
 */
final class ReportGeneratorForFiles implements IReportGenerator
{

    /**
     * Saves the statistics for the employees
     *
     * @param personnel: list of employees
     * @return 0 if all goes well, a negative number if sth goes wrong
     */
    @Override
    public int reportOnEmps(List<Employee> personnel)
    {
        if (Objects.isNull(personnel))
        {
            System.err.println("ReportGeneratorForFiles: null object in report for Emps");
            return -1;
        }

        PrintWriter outputStream   = null;
        String      outputFileName = "./src/main/resources/output/empReport.ascii";

        //Opening files for read and write, checking exception
        try
        {
            outputStream = new PrintWriter(new FileOutputStream(outputFileName));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Problem opening Emp report.");
            return -1;
            //System.exit(0);
        }

        for (Employee e : personnel)
        {
            outputStream.println(e.getShortReport());
        }

        outputStream.close();
        return 0;
    }


    /**
     * Saves the statistics for the dishes
     *
     * @param menu: list of dishes
     * @return 0 if all goes well, a negative number if sth goes wrong
     */
    @Override
    public int reportOnDishes(List<Dish> menu)
    {
        if (Objects.isNull(menu))
        {
            System.err.println("ReportGeneratorForFiles: null object in report for Dishes");
            return -1;
        }
        PrintWriter outputStream   = null;
        String      outputFileName = "./src/main/resources/output/dishReport.ascii";

        //Opening files for read and write, checking exception
        try
        {
            outputStream = new PrintWriter(new FileOutputStream(outputFileName));
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Problem opening Dish report.");
            return -1;
            //System.exit(0);
        }

        for (Dish d : menu)
        {
            outputStream.println(d.getShortReport());
        }

        outputStream.close();
        return 0;
    }

}
