package model;
/**
* <h1>SousChef</h1>
* Class responsible for handling the information
* about a sous chef.
*
* @version 1.0
* @since   2017-07-22
*/
public class SousChef extends Employee {

	public SousChef() {
		// TODO Auto-generated constructor stub
	}

	public SousChef(String fName, String mName, String lName) {
		super(fName, mName, lName);
	}

	/**
 	 * Computes the salary of the sous chef
 	 * 
 	 * @return the salary of the sous chef
 	 */
	@Override
	public double computeSalary(){
		salary = SousChef.salaryFixedAmt + SousChef.salaryRate * evaluation;
		return salary;
	}
	private static int salaryRate = 100;
	private static double salaryFixedAmt = 2000.0;
}
