package load;

import java.util.List;

import model.*;
/**
* <h1>IFullDataLoader</h1>
* Interface responsible for reading the data from
* all input files.
*
* @version 1.0
* @since   2017-07-22
*/
public interface IFullDataLoader {
	/**
 	 * Loads the three different types of files and stores
 	 * the data in the three different lists that receives 
 	 * as parameters.
 	 * 
 	 * @param personnel: empty list holding information about employees
 	 * @param menu: empty list holding information about dishes
 	 * @param orders: empty list holding information about orders
 	 * 
 	 * @return 0 if all works out well, negative otherwise
 	 */
	public abstract int performFullDataLoad(List<Employee> personnel, List<Dish> menu, List<OrderItem> orders);
}
