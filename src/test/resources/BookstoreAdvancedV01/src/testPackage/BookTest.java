package testPackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bookstore.Book;

public class BookTest {
	private Book bookToTest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//this runs once, before _all_ tests. Nothing todo here.
		//See CDTest for explanations
	}

	@Before
	public void setUp() throws Exception {
		bookToTest = new Book("Discours de la methode", "Rene Descartes", 1637, 50.00, 0, 1);
		//this runs before _each_ test. create a book to test constructor, price and final price 
	}



	@Test
	public final void testBookNull() {
		//remember: the setUp() method has run already: Book should have been initialized!
		assertNotNull("After setup, the book is not null", bookToTest);
		
		//at the beginning to see that the test works.
		//fail("Not yet implemented");
	}

	@Test
	public final void testBookNoTitle() {
	
		//See how this test intentionally fails. It gets a null title. 
		//The factory should trap this and avoid creating a book without a tile. 
		bookToTest = new Book(null, "Rene Descartes", 1637, 50.00, 0, 2);
		assertNull("test if the constructor prevents creation with null title", bookToTest);
				
		//at the beginning to see that the test works.
		//fail("Not yet implemented");
	}
	
	
	@Test
	public final void testBookNegativePrice() {
		//Fails: should do the same with null title. Again, intentionally put to show failure!
		// MUST CHECK THIS IN THE FACTORY, BEFORE PASSING IT TO THE CONSTRUCTOR! 
		bookToTest = new Book("Discours de la methode", "Rene Descartes", 1637, -12.00, 0, 3);
		assertNull("test if the constructor prevents creation with negative price", bookToTest);
		
		//at the beginning to see that the test works.
		//fail("Not yet implemented");
	}
	

	@Test
	public final void testGetFinalPrice() {
		assertEquals("test if getFinalPrice() works OK", 50.00, bookToTest.getFinalPrice(), 3);
		//fail("Not yet implemented"); 
	}
	
	
	@Test
	public final void testGetPrice() {
		assertEquals("test if getOriginalPrice of the ITEM CLASS works OK", 50.00, bookToTest.getOriginalPrice(), 3);
		//fail("Not yet implemented"); 
	}

}
