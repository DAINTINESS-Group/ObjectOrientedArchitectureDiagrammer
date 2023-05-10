package model;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import load.IFullDataLoader;
import load.FullDataLoaderFactory;

public class DataModelTest {
	private EmployeeFactory employeeFactory;
	private ArrayList<Employee> personnel;
	private ArrayList<Dish> menu;
	private ArrayList<OrderItem> orders;
	private IFullDataLoader dataLoader;
	private FullDataLoaderFactory dataLoaderFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//this runs once, before _all_ tests. Nothing todo here.
		//See CDTest for explanations
	}

	@Before
	public void setUp() throws Exception {
		employeeFactory = new EmployeeFactory();
		personnel = new ArrayList<Employee>();
		menu = new ArrayList<Dish>();
		orders = new ArrayList<OrderItem>();
		dataLoaderFactory = new FullDataLoaderFactory();
		dataLoader = dataLoaderFactory.createFullDataLoader("FullDataLoader",
				"./src/test/resources/input/" + "employees_test.txt", 
				"./src/test/resources/input/" + "dishes_test.txt", 
				"./src/test/resources/input/" + "orders_test.txt");	
	}

	@Test
	public final void testEmployeeFactoryNull() {
		assertNotNull("After setup, the employeeFactory is not null", employeeFactory);
	}

	
	@Test
	public final void testDataRecords() {
		dataLoader.performFullDataLoad(personnel, menu, orders);
		
		for(OrderItem order: orders){
			assertNotNull(order.getOrderEmp());
			assertNotNull(order.getOrderDish());
		}
		
		for(Dish dish:menu){
			assertNotNull(dish.getOrders());
		}	
	}
	
}
