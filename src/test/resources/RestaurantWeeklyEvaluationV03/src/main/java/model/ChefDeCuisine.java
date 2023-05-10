package model;
/**
* <h1>ChefDeCuisine</h1>
* Class responsible for handling the information
* about a chef de cuisine.
*
* @version 1.0
* @since   2017-07-22
*/
public class ChefDeCuisine extends Employee {

	public ChefDeCuisine() {
		// TODO Auto-generated constructor stub
	}

	public ChefDeCuisine(String fName, String mName, String lName) {
		super(fName, mName, lName);
		// TODO Auto-generated constructor stub
	}
//TODO FIX FIX FIX
	
	/**
 	 * Computes the salary of the chef de cuisine
 	 * 
 	 * @return the salary of the chef de cuisine
 	 */
	@Override
	public double computeSalary(){
		double bonus = 0;
		if(WeeklyStats.getTotalAvgStars() > 4){
			bonus = 0.02 * WeeklyStats.getTotalGain();
		}
		return bonus + ChefDeCuisine.fixedAmt;
	}
	
	private static double fixedAmt = 2000;
}
