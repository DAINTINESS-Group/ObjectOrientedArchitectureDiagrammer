package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeTest {
	private EmployeeFactory employeeFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//this runs once, before _all_ tests. Nothing todo here.
		//See CDTest for explanations
	}

	@Before
	public void setUp() throws Exception {
		employeeFactory = new EmployeeFactory();
	}

	@Test
	public final void testEmployeeFactoryNull() {
		assertNotNull("After setup, the employeeFactory is not null", employeeFactory);
	}
	
	@Test
	public final void testEmployeeRecords() {
		Employee chef = employeeFactory.createEmp("Joe", "", "Doe", "Chef");
		assertTrue(chef instanceof Chef);
		
		chef = employeeFactory.createEmp("Jack", "", "Doe", "SousChef");
		assertTrue(chef instanceof SousChef);
		
		chef = employeeFactory.createEmp("William", "", "Doe", "ChefDeCuisine");
		assertTrue(chef instanceof ChefDeCuisine);
		
		chef = employeeFactory.createEmp("Averell", "", "Doe", "WrongType");
		assertEquals(chef,null);
		
	}
}
