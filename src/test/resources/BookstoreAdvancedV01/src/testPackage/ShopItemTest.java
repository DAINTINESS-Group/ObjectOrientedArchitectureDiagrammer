package testPackage;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gui.ShopItem;
/**
 * Test class to test the new junit class. Kill with power.
 * 
 * @version 2018-11-03
 * @author pvassil
 *
 */
public class ShopItemTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testShopItem() {
		//Intentionally fails 
		ShopItem shopItem = new ShopItem(null, 120.00,1);
		assertNotNull("ShopItem: title not null", shopItem.getTitle()); //fails due to null title
		
		Boolean flag = (Double.parseDouble(shopItem.getPrice()) >= 0.0) ? true : false;
		assertTrue("price positive", flag);
		//fail("Not yet implemented"); 
	}

}
