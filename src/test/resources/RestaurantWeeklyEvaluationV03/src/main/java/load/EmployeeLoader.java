package load;

import java.util.List;

import model.Employee;
import model.EmployeeFactory;

/**
 * <h1>DishLoader</h1>
 * Class responsible for reading the data from an input file that contains information about employees.
 * <p>
 * The class is final: cannot be subclassed; if need, create a new v. of the class and exploit the factory for its generation
 * <p>
 * Observe the package-private visibility: the class is visible to the other classes of its package (tests of the package included) but not visible outside the package. Only the public interface and its factory are visible to others.
 *
 * @version 1.0
 * @since 2017-07-22
 */
final class EmployeeLoader extends AbstractRecordLoader<Employee>
{

//	public EmployeeLoader() {
//		// TODO Auto-generated constructor stub
//	}


    /**
     * Adds a record in the input list given the tokens of a row.
     *
     * @param tokens:    a list of fields from a row of the input file
     * @param personnel: list which holds the information about the employees
     * @return 0
     */
    @Override
    public int constructObjectFromRow(String[] tokens, List<Employee> personnel)
    {
        if (tokens == null || personnel == null)
            return -1;

        String          firstName;
        String          middleName;
        String          lastName;
        String          rank;
        EmployeeFactory empFactory = new EmployeeFactory();

        firstName = tokens[0];
        middleName = tokens[1];
        lastName = tokens[2];
        rank = tokens[3];

        //Employee e = new Employee(firstName, middleName, lastName);
        Employee e = empFactory.createEmp(firstName, middleName, lastName, rank);

        personnel.add(e);

        return 0;
    }

}
