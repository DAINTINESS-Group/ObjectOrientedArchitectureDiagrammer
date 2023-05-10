package testPackage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import bookstore.Book;
import bookstore.Item;
import bookstore.ItemManager;

public class ItemManagerTest {

	@Before
	public void setUp() throws Exception {
		//this is supposed to run before _all_ tests
		//nothing to set up here
	}

	@Test
	public void testItemManager() {
		ItemManager amazon = new ItemManager();
		assertNotNull(amazon.getAllItems());
		
		//before implementing the above, try having just the following fail.
		//Run the test and see that it fails indeed.
		//Then build your test.
		//fail("Intentional failure. Not yet implemented");
	}

	@Test
	public void testAddItem() {
		ItemManager amazon = new ItemManager();
		assertEquals(0,amazon.getAllItems().size());
		
		Book bookRef;
		bookRef = new Book("Discours de la methode", "Rene Descartes", 1637, 50.00, 0, 9); 
		amazon.addItem(bookRef);
		assertNotEquals(0,amazon.getAllItems().size());
		assertEquals(1,amazon.getAllItems().size());
		
		//fail("Not yet implemented");
	}

	@Test
	public void testReportAllItems() {
		ItemManager itemManager = new ItemManager();

		Item item = new Book("Discours de la methode","Rene Descartes", 1637, 50.00, 0, 0);
		itemManager.addItem(item);
		item = new Book("The Meditations",   "Marcus Aurelius", 180,30.00, 1,1);
		itemManager.addItem(item);
		
		String expectedResult = "***Item id: 0***\n" +
		"Discours de la methode		 Price:50.0\n" +
		" by Rene Descartes at 1637\n" +
		"with simple packaging.\n" +
		"\n" +
		"***Item id: 1***\n" +
		"The Meditations		 Price:30.0\n" +
		" by Marcus Aurelius at 180\n" +
		"with hard packaging.\n" +
		"\nTotal number of items: 2\n";
		assertEquals(expectedResult, itemManager.reportAllItems());
	}

}
