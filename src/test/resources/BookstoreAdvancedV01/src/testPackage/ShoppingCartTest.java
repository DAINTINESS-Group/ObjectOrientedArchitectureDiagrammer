package testPackage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import bookstore.Book;
import bookstore.Item;
import bookstore.ShoppingCart;

public class ShoppingCartTest {
	
	@Before
	public void setUp() throws Exception {
		//this is supposed to run before _all_ tests
		//nothing to set up here
	}

	@Test
	public void testItemManager() {
		ShoppingCart cart = new ShoppingCart();
		assertNotNull(cart.getItems());
		
	}

	@Test
	public void testAddItem() {
		ShoppingCart cart = new ShoppingCart();
		assertEquals(0,cart.getItems().size());
		
		Book bookRef;
		bookRef = new Book("Discours de la methode", "Rene Descartes", 1637, 50.00, 0, 9); 
		cart.addItem(bookRef);
		assertNotEquals(0,cart.getItems().size());
		assertEquals(1,cart.getItems().size());
		
	}
	
	@Test
	public void testRemoveItem(){
		//intentionally fails if the last, removal command is used with id different than the one added!
		ShoppingCart cart = new ShoppingCart();
		assertEquals(0,cart.getItems().size());
		
		Book bookRef;
		bookRef = new Book("Discours de la methode", "Rene Descartes", 1637, 50.00, 0, 9); 
		cart.addItem(bookRef);
		assertNotEquals(0,cart.getItems().size());
		assertEquals(1,cart.getItems().size());
		
		cart.removeItem(0);
		assertEquals(0,cart.getItems().size());	//intentionally fails. the book has id=9 and not 0.
	}
	
	@Test
	public void testShowItems(){
		ShoppingCart cart = new ShoppingCart();
		
		Item item = new Book("Discours de la methode","Rene Descartes", 1637, 50.00, 0, 0);
		cart.addItem(item);
		item = new Book("The Meditations",   "Marcus Aurelius", 180,30.00, 1,1);
		cart.addItem(item);
		
		String expectedResult = 
				"------------------------\n" +
				"       CART ITEMS       \n" +
				"------------------------\n" +
				"***Item id: 0***\n" +
				"Discours de la methode		 Price:50.0\n" +
				" by Rene Descartes at 1637\n" +
				"with simple packaging.\n" +
				"\n" +
				"***Item id: 1***\n" +
				"The Meditations		 Price:30.0\n" +
				" by Marcus Aurelius at 180\n" +
				"with hard packaging.\n\n" +
				"Total cost: 80.0\n";
		assertEquals(expectedResult, cart.showDetails());
		
	}
	
	
}
