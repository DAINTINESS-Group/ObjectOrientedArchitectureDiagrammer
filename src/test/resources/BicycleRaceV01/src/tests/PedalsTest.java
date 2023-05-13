package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bicyclePedal.IPedal;
import bicyclePedal.PedalFactory;
import bicyclePedal.SimplePedal;
import bicyclePedal.ZuperPedal;

public class PedalsTest {

	private PedalFactory pedalsFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
	}

	@Before
	public void setUp() throws Exception {
		pedalsFactory = new PedalFactory();	
	}

	@Test
	public final void testPedalsFactoryNull() {
		assertNotNull("After setup, the pedalsFactory is not null", pedalsFactory);
	}

	
	@Test
	public final void testBrakeCreation() {
		IPedal brake = pedalsFactory.constructPedal("SimplePedal");
		assertTrue(brake instanceof SimplePedal);
		
		brake = pedalsFactory.constructPedal("ZuperPedal");
		assertTrue(brake instanceof ZuperPedal);
		
		brake = pedalsFactory.constructPedal("wrong type");
		assertEquals(brake,null);
	}
}
