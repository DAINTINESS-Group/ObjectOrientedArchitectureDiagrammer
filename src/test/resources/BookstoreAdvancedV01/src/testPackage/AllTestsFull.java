package testPackage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Just to prove that a simple create JUnit test suite works. Can kill
 * @version 2018-11-3
 * @author pvassil
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ BookTest.class, CDTest.class, ItemManagerTest.class, ShopItemTest.class,
		ShoppingCartTest.class })
public class AllTestsFull {

}
