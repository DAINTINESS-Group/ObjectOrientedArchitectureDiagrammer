package controller;

import javafx.collections.ObservableList;
import model.Dish;
import model.Employee;

/**
 * <h1>IMainEngine</h1>
 * Interface which provides a contract to the clients regarding (a) the load of the input files, (b) the computation of the statistics and (c) the creation of reports
 *
 * @version 1.0
 * @since 2017-07-22
 */
public interface IMainEngine
{

    /**
     * Loads the data from the input files
     *
     * @return return 0 if executed properly, negative otherwise
     */
    public int loadAllData();

    /**
     * Computes the weekly statistics and the statistics for each employee and prints the results
     */
    public void computeAllStats();

    /**
     * Creates the different types of reports
     *
     * @param mode a String to denote how to output reports. Valid values: "ReportGeneratorForFiles" / "ReportGeneratorForHTML"
     * @return 0 if all goes well, non-zero otherwise
     */
    public int createReports(String mode);

    /**
     * Accepts an Observable List of Employees and fills it
     *
     * @param data an Observable List of Employees
     * @return the size of the list, if all is OK, -1 otherwise
     */
    public int getEmployees(ObservableList<Employee> data);

    /**
     * Accepts an Observable List of Dishes and fills it
     *
     * @param data an Observable List of Dishes
     * @return the size of the list, if all is OK, -1 otherwise
     */
    public int getDishes(ObservableList<Dish> data);
}
