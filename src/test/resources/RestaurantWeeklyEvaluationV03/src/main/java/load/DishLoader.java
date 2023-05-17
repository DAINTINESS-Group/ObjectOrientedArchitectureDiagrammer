package load;


import java.util.List;

import model.Dish;
/**
* <h1>DishLoader</h1>
* Class responsible for reading the data from
* an input file that contains information about
* dishes.
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
final class DishLoader extends AbstractRecordLoader<Dish>{


//	public DishLoader() {
//		// TODO Auto-generated constructor stub
//	}

	/**
 	 * Adds a record in the input list given the tokens of a
 	 * row.
 	 * 
 	 * @param tokens: a list of fields from a row of the input file
 	 * @param menu: list which holds the information about the dishes
 	 * 
 	 * @return 0
 	 */
	@Override
	public int constructObjectFromRow(String [] tokens, List<Dish> menu){
		if(tokens == null || menu == null)
			return -1;
		int id;
		String name;
		double cost;
		double price;
		double gain;

		id = Integer.parseInt(tokens[0]);
		name = tokens[1];
		cost = Double.parseDouble(tokens[2]);
		price = Double.parseDouble(tokens[3]);
		gain = price - cost;

		Dish d;
		d = new Dish(id, name, gain);
		menu.add(d);	

		return 0;

	}

}
