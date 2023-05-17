package model;
/**
* <h1>WeeklyStats</h1>
* Class responsible for handling the weekly
* statistics of the restaurant.
*
* @version 1.0
* @since   2017-07-22
*/
public class WeeklyStats {

	private static int numEmployees;
	private static int numDishes;
	private static int numOrders;
	private static double totalGain;
	private static double totalAvgStars;
	
//	public WeeklyStats() {
//		// TODO Auto-generated constructor stub
//	}

	public static int getNumEmployees() {
		return numEmployees;
	}

	public static void setNumEmployees(int numEmployees) {
		WeeklyStats.numEmployees = numEmployees;
	}

	public static int getNumDishes() {
		return numDishes;
	}
	public static void setNumDishes(int numDishes) {
		WeeklyStats.numDishes = numDishes;
	}
	public static int getNumOrders() {
		return numOrders;
	}
	public static void setNumOrders(int numOrders) {
		WeeklyStats.numOrders = numOrders;
	}
	public static double getTotalGain() {
		return totalGain;
	}
	public static void setTotalGain(double totalGain) {
		WeeklyStats.totalGain = totalGain;
	}
	public static double getTotalAvgStars() {
		return totalAvgStars;
	}
	public static void setTotalAvgStars(double totalAvgStars) {
		WeeklyStats.totalAvgStars = totalAvgStars;
	}

	
}
