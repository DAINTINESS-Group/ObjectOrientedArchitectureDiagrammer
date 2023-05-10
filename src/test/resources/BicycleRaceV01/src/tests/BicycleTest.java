package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bicycleBody.Bicycle;


public class BicycleTest {
	
	private Bicycle bicycle;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
	}

	@Before
	public void setUp() throws Exception {
		bicycle = new Bicycle("SimplePedal","DuperBrakes", "Test1");	
	}

	@Test
	public final void testBicycleNull() {
		assertNotNull("After setup, the bicycle is not null", bicycle);
	}

	
	@Test
	public final void testBrakeCreation() {
		assertEquals("Test if getVelocity() works OK", 0.00, bicycle.getVelocity(),1);
		bicycle.setOriginalVelocity(50);
		assertEquals("Test if getVelocity() works OK", 50.00, bicycle.getVelocity(),1);
		bicycle.setPedaling(10);
		assertEquals("Test if getVelocity() works OK", (50.0+35*10), bicycle.getVelocity(),1);
		
		//Resets velocity
		bicycle.setOriginalVelocity(50);
		bicycle.setBraking(20);
		assertEquals("Test if getVelocity() works OK", 0.0 , bicycle.getVelocity(),1);	
	}
	
	@Test
	public final void testTimeComputation() {
		bicycle.setOriginalVelocity(10);
		double distance = 1000.0;
		bicycle.computeTime(distance);
		assertEquals(100, (int)bicycle.getTimeRun());
	}
	
}
