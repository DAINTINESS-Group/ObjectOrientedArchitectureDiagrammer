package controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;


import javafx.collections.ObservableList;

import reporting.IReportGenerator;
import reporting.ReporterFactory;
import load.IFullDataLoader;
import load.FullDataLoaderFactory;
import model.*;

/**
* <h1>Main Engine</h1>
* Class responsible for (a) the load of the input files, (b) the 
* computation of the statistics and (c) the creation
* of reports
*
* The class is final: cannot be subclassed; if need, create a new v. of the class
* and exploit the factory for its generation
*
* Observe the package-private visibility: the class is visible
* to the other classes of its package (tests of the package included)
* but not visible outside the package.
* Only the public interface and its factory are visible to others.
*
* @version 1.0
* @since   2017-07-22
*/
final class MainEngine implements IMainEngine{
	
	private List<Employee> personnel;
	private List<Dish> menu;
	private List<OrderItem> orders;
	private IFullDataLoader dataLoader;
	private FullDataLoaderFactory dataLoaderFactory;

// Looser coupling: instead of using them as fields, I create an IReportGenerator ON-DEMAND within createReports()
//	private IReportGenerator reporter;
//	private ReporterFactory reporterFactory;
	
	
	public MainEngine(){
		personnel = new ArrayList<Employee>();
		menu = new ArrayList<Dish>();
		orders = new ArrayList<OrderItem>();
		dataLoaderFactory = new FullDataLoaderFactory();
		dataLoader = dataLoaderFactory.createFullDataLoader("FullDataLoader", "./src/main/resources/input/employees.txt", 
				"./src/main/resources/input/dishes.txt", "./src/main/resources/input/orders.txt");
	}

	public MainEngine(String empPath, String dishPath, String orderPath) throws NullPointerException{
		if(Objects.isNull(empPath) || Objects.isNull(dishPath) || Objects.isNull(orderPath)) {
			throw new NullPointerException();
		}
		
		personnel = new ArrayList<Employee>();
		menu = new ArrayList<Dish>();
		orders = new ArrayList<OrderItem>();
		dataLoaderFactory = new FullDataLoaderFactory();
		dataLoader = dataLoaderFactory.createFullDataLoader("FullDataLoader", empPath, dishPath, orderPath);
	}
	
	/**
 	 * Loads the data from the input files
 	 * 
 	 */
	@Override
	public int loadAllData(){
		return dataLoader.performFullDataLoad(personnel, menu, orders);		
	}
	
	/**
 	 * Computes the weekly statistics and the statistics
 	 * for each employee and prints the results
 	 * 
 	 */
	@Override
	public void computeAllStats(){
		double totalGain = 0.0;
		double totalNumStars = 0.0;
		
		for(Dish d: menu){
			d.computeDishStats();
			totalGain += d.getGain();
			totalNumStars += d.getEvaluation();	
		}

		//MUST compute them BEFORE Emp Stats: Chef De cuisine depends on these numbers!
		WeeklyStats.setNumEmployees(personnel.size());
		WeeklyStats.setNumDishes(menu.size());
		WeeklyStats.setNumOrders(orders.size());
		WeeklyStats.setTotalGain(totalGain);
		WeeklyStats.setTotalAvgStars(totalNumStars/menu.size());  //totalNumStars is double, so the division is OK
		
		for(Employee e: personnel){
			e.computeEmpStats();			
		}

		System.out.println("\n\nEmps: " + WeeklyStats.getNumEmployees() + "\tDishes: "+WeeklyStats.getNumDishes() +"\tOrders: " + WeeklyStats.getNumOrders() + "\tTotalGain: " + WeeklyStats.getTotalGain() + "\tAvgStars: " + WeeklyStats.getTotalAvgStars());
	}
	
	/**
 	 * Creates the different types of reports
 	 * 
 	 */
	@Override
	public int createReports(String mode){
		ReporterFactory reporterFactory = new ReporterFactory();
		IReportGenerator reporter = reporterFactory.createReporter(mode);
		int dishReportFlag = reporter.reportOnDishes(menu);
		int empsReportFlag = reporter.reportOnEmps(personnel);
		
		if((0 == dishReportFlag) &&(0 == empsReportFlag))
			return 0;
		else
			return -1;
	}
	
	@Override
	public int getEmployees(ObservableList<Employee> data){
		if(Objects.isNull(data))
			return -1;
		data.addAll(personnel);
		return data.size();
	}
	
	@Override
	public int getDishes(ObservableList<Dish> data){
		if(Objects.isNull(data))
			return -1;		
		data.addAll(menu);
		return data.size();
	}

}
