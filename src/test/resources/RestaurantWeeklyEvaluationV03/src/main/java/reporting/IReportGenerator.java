package reporting;

import java.util.List;

import model.Employee;
import model.Dish;

/**
 * <h1>IReportGenerator</h1>
 * Interface which provides a contract to the clients regarding (a) reports for employees and (b) reports for dishes
 *
 * @version 1.0
 * @since 2017-07-22
 */
public interface IReportGenerator
{
    /**
     * Saves the statistics for the employees to a standard location
     *
     * @param personnel: list of employees
     * @return 0 if all goes well, a negative number if sth goes wrong
     */
    public abstract int reportOnEmps(List<Employee> personnel);

    /**
     * Saves the statistics for the dishes to a standard location
     *
     * @param menu: list of dishes
     * @return 0 if all goes well, a negative number if sth goes wrong
     */
    public abstract int reportOnDishes(List<Dish> menu);
}
