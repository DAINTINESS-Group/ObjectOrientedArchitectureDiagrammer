package testPackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bookstore.CD;

public class CDTest {

	private static CD cdToTest;	//attn, must be static because we will use it at beforeClass!
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//this runs once, before _all_ tests. Here we create a single object (to save time)
		//and use it in all tests.
		cdToTest = new CD("Piece of Mind", 10.0,"Iron Maiden",4.0, 7);
	}
	
	@Before
	public void setUp() throws Exception {
		//see BookTest for explanations. 
	}

	//Also here, we omit the tests for null / invalid construction values, as we did in Book
	
	@Test
	public final void testGetFinalPrice() {
		//CD cdToTest = new CD("Piece of Mind", 10.0,"Iron Maiden",4.0);	
		assertEquals("test if getFinalPrice() works OK", 6.00, cdToTest.getFinalPrice(), 3);

		//fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetPrice() {
		//CD cdToTest = new CD("Piece of Mind", 10.0,"Iron Maiden",4.0);	
		assertEquals("test if getPrice() of the ITEM CLASS works OK", 10.00, cdToTest.getOriginalPrice(), 3);

		//fail("Not yet implemented"); // TODO
	}

}
