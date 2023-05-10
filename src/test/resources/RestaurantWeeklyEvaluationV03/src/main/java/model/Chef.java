package model;
/**
* <h1>Chef</h1>
* Class responsible for handling the information
* about a chef.
*
* @version 1.0
* @since   2017-07-22
*/
public class Chef extends Employee {
	private static int salaryRate = 400;
	
	public Chef(String fName, String mName, String lName) {
		super(fName, mName, lName);
	}

	/**
 	 * Computes the salary of the chef
 	 * 
 	 * @return the salary of the chef
 	 */
	@Override
	public double computeSalary(){
		salary = Chef.salaryRate * evaluation;
		return salary;
	}

}
