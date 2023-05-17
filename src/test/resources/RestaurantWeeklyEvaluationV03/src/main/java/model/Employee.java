package model;

import java.util.ArrayList;

/**
* <h1>Employee</h1>
* Abstract class responsible for handling the information
* about an employee.
*
* @version 1.0
* @since   2017-07-22
*/
public abstract class Employee {
	protected String firstName;
	protected String middleName;
	protected String lastName;
	protected ArrayList<OrderItem> orders;
	protected double salary;
	protected double evaluation;

	public Employee() {
		orders = new ArrayList<OrderItem>();
	}
	public Employee(String fName, String mName, String lName) {
		firstName = fName;
		middleName = mName;
		lastName = lName;
		orders = new ArrayList<OrderItem>();
	}
	
/*	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public void setEvaluation(double evaluation) {
		this.evaluation = evaluation;
	}
	public ArrayList<OrderItem> getOrders() {
		return orders;
	}
	*/
	
	/**
 	 * Computes the salary of the employee
 	 * 
 	 * @return the salary of the employee
 	 */
	public abstract double computeSalary();
	
	/**
 	 * Adds an order to the list of orders
 	 * 
 	 * @param o: an order
 	 */
	public void addOrder(OrderItem o){
		orders.add(o);
	}

	/**
 	 * Computes the statistics for an employee
 	 * 
 	 * @return the number of orders
 	 */
	public int computeEmpStats(){
		double sumStars = 0.0;
		int count = 0;
		
		if (orders.isEmpty() == false){
			for (OrderItem o: orders){
				count++;
				sumStars += o.getStars();
			}
		}
		evaluation = sumStars /  (count);
		salary = computeSalary();
		return count;
	}
	
	/**
 	 * 
 	 * @return a short report regarding the employee
 	 */
	public String getShortReport(){
		return getName() + "\t NumOrders: " + orders.size() + "\t Stars: " + evaluation + "\t Salary: " + salary;
	}
	//intentionally just the first name
	public String getName(){
		return firstName;
	}
	public double getSalary() {
		return salary;
	}
	public double getEvaluation() {
		return evaluation;
	}
	public int getNumberOfOrders(){
		return orders.size();
	}

}
