package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.Dish;
import model.Employee;
import model.WeeklyStats;


public class MainEngineTest {
	
	private MainEngine mainEngine;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
	}

	@Before
	public void setUp() throws Exception {
		try{
			mainEngine  = new MainEngine("./src/test/resources/input/" + "employees_test.txt", 
					"./src/test/resources/input/" + "dishes_test.txt", 
					"./src/test/resources/input/" + "orders_test.txt");
		}
		catch(NullPointerException e) {
			System.err.println("Cannot create main engine due to the absence of input files, exiting!");
			System.exit(0);
		}
	}
	@Test
	public final void testLoadAllData() {
		mainEngine.loadAllData();
		ObservableList<Employee> emps = FXCollections.observableArrayList();
		ObservableList<Dish> dishes = FXCollections.observableArrayList();
		mainEngine.getDishes(dishes);
		mainEngine.getEmployees(emps);
		assertEquals(emps.size(), 2);
		assertEquals(dishes.size(), 4);
	}
	
	@Test
	public final void testComputeAllStats() {
		mainEngine.loadAllData();
		mainEngine.computeAllStats();
		
		assertEquals(WeeklyStats.getNumEmployees(), 2);
		assertEquals(WeeklyStats.getNumDishes(), 4);
		assertEquals(WeeklyStats.getNumOrders(), 4);
		assertEquals(WeeklyStats.getTotalGain(), 11.5, 2);
		assertEquals(WeeklyStats.getTotalAvgStars(), 4.25, 2);
	}

	@Test
	public final void testCreateReports() {
		//fail("Not yet implemented"); // TODO
	}

}
