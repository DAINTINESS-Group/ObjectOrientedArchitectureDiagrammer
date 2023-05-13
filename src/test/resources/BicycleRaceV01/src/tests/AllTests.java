package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BicycleTest.class, BrakesTest.class, PedalsTest.class })
public class AllTests {
	//no need to add sth here. The above directives simply run all tests
}
