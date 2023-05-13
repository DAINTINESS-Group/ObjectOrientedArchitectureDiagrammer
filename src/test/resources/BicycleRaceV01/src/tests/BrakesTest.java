package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bicycleBreaks.BrakeFactory;
import bicycleBreaks.DuperBrakes;
import bicycleBreaks.IBrakes;
import bicycleBreaks.NiceBrakes;



public class BrakesTest {
	
	private BrakeFactory brakeFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
	}

	@Before
	public void setUp() throws Exception {
		brakeFactory = new BrakeFactory();	
	}

	@Test
	public final void testBreaksFactoryNull() {
		assertNotNull("After setup, the breaksFactory is not null", brakeFactory);
	}

	
	@Test
	public final void testBrakeCreation() {
		IBrakes brake = brakeFactory.constructBrake("NiceBrakes");
		assertTrue(brake instanceof NiceBrakes);
		
		brake = brakeFactory.constructBrake("DuperBrakes");
		assertTrue(brake instanceof DuperBrakes);
		
		brake = brakeFactory.constructBrake("wrong type");
		assertEquals(brake,null);
	}
	
}
