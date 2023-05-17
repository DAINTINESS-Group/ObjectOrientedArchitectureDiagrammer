package load;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.Dish;
import model.Employee;
import model.OrderItem;



public class RecordLoaderTest {
	private OrderLoader orderLoader;
	private DishLoader dishLoader;
	private EmployeeLoader employeeLoader;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//this runs once, before _all_ tests. Nothing todo here.
		//See CDTest for explanations
	}

	@Before
	public void setUp() throws Exception {
		dishLoader = new DishLoader();
		orderLoader = new OrderLoader();
		employeeLoader = new EmployeeLoader();
	}

	@Test
	public final void testDishLoaderNull() {
		assertNotNull("After setup, the dish is not null", dishLoader);
	}
	
	@Test
	public final void testOrderLoaderNull() {
		assertNotNull("After setup, the order is not null", dishLoader);
	}
	
	@Test
	public final void testEmployeeNull() {
		assertNotNull("After setup, the employee is not null", dishLoader);
	}

	@Test
	public final void testDishLoaderRecords() {
		String dishFile = "./src/test/resources/input/" + "dishes_test.txt";
		ArrayList<Dish> menu = new ArrayList<Dish>();
		assertNotEquals(0,dishLoader.load(dishFile, "\t", true, 4, menu));
		assertEquals(4,menu.size());
		
		assertEquals(5,dishLoader.load(dishFile, "\t", true, 4, menu));	
	}
	
	@Test
	public final void testOrderLoaderRecords() {
		String orderFile = "./src/test/resources/input/" +"orders_test.txt";
		ArrayList<OrderItem> orders = new ArrayList<OrderItem>();
		assertNotEquals(0,orderLoader.load(orderFile, "\t", true, 5, orders));
		
		assertEquals(4,orders.size());
		
		assertEquals(5,orderLoader.load(orderFile, "\t", true, 5, orders));
		
	}
	
	@Test
	public final void testEmployeeLoaderRecords() {
		String dishFile = "./src/test/resources/input/" + "/employees_test.txt";
		ArrayList<Employee> empl = new ArrayList<Employee>();
		assertNotEquals(0,employeeLoader.load(dishFile, "\t", true, 4, empl));
		
		assertEquals(2,empl.size());
		
		assertEquals(3,employeeLoader.load(dishFile, "\t", true, 4, empl));
	}

}
