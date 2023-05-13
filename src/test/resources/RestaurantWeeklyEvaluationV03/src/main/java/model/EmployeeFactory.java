package model;
/**
* <h1>EmployeeFactory</h1>
* factory class responsible for creating different types
* of employees
*
* @version 1.0
* @since   2017-07-22
*/
public class EmployeeFactory {


	
	/**
 	 * Creates an employee based on the type
 	 * 
 	 * @param fName: first name
 	 * @param mName: middle name
 	 * @param lName: last name
 	 * @param type: employee type
 	 * 
 	 * @return an object of type Employee
 	 */
	public Employee createEmp(String fName, String mName, String lName, String type){
		switch(type){
		case "Chef": 
			return new Chef(fName, mName, lName);
			//break;
		case "SousChef": 
			return new SousChef(fName, mName, lName);
			//break;
		case "ChefDeCuisine": 
			return new ChefDeCuisine(fName, mName, lName);
			//break;
		default:
			System.out.println("If the code got up to here, you passed a wrong argument to the EmployeeFactory Factory");
			return null;
		}
		
	}
}
