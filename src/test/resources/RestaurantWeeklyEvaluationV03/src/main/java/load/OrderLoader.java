package load;

import java.util.List;

import model.OrderItem;
/**
* <h1>DishLoader</h1>
* Class responsible for reading the data from
* an input file that contains information about
* orders.
* 
* The class is final: cannot be subclassed; if need, create a new v. of the class
* and exploit the factory for its generation
*
* Observe the package-private visibility: the class is visible
* to the other classes of its package (tests of the package included)
* but not visible outside the package.
* Only the public interface and its factory are visible to others.
* 
* @version 1.0
* @since   2017-07-22
*/
 final class OrderLoader extends AbstractRecordLoader<OrderItem> {

//	public OrderLoader() {
//		// TODO Auto-generated constructor stub
//	}

	/**
 	 * Adds a record in the input list given the tokens of a
 	 * row.
 	 * 
 	 * @param tokens: a list of fields from a row of the input file
 	 * @param orders: list which holds the information about the orders
 	 * 
 	 * @return 0
 	 */
	@Override
	public int constructObjectFromRow(String[] tokens, List<OrderItem> orderItems) {
		if(tokens == null || orderItems == null)
			return -1;

		int id;
		String dishName;
		String empName;
		double timeToMake;
		int stars;
		
		id = Integer.parseInt(tokens[0]);
		dishName = tokens[1];
		empName = tokens[2];
		timeToMake = Double.parseDouble(tokens[3]);
		stars = Integer.parseInt(tokens[4]);
		
		
		OrderItem o = new OrderItem(id, dishName, empName, timeToMake, stars);
		orderItems.add(o);	
		return 0;
	}

}
