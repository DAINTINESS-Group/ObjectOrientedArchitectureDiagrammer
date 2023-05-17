package load;

//import java.io.IOException;
import java.util.List;



import model.*;
/**
* <h1>FullDataLoader</h1>
* Class responsible for reading the data from
* all input files.
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
 final class FullDataLoader implements IFullDataLoader {
	//private IEmpLoader empLoader;
	//private IDishLoader dishLoader;
	//private IOrderLoader orderLoader;
	private String empFile;
	private String dishFile;
	private String orderFile;
	
	private DishLoader dishLoader;
	private EmployeeLoader empLoader;
	private OrderLoader orderLoader;
	
	public FullDataLoader(){

		empFile = "./src/main/resources/input/employees.txt";
		empLoader = new EmployeeLoader();

		dishFile = "./src/main/resources/input/dishes.txt";
		dishLoader = new DishLoader();

		orderFile = "./src/main/resources/input/orders.txt";
		orderLoader = new OrderLoader();

	}
	public FullDataLoader(String empPath, String dishPath, String orderPath){
		this.empFile = empPath;
		empLoader = new EmployeeLoader();
		this.dishFile = dishPath;
		dishLoader = new DishLoader();
		this.orderFile = orderPath;
		orderLoader = new OrderLoader();
	}
	/**
 	 * Creates the mapping between the three different entities
 	 * (Employee, Dish, OrderItem)
 	 * 
 	 * @param personnel: list holding information about employees
 	 * @param menu: list holding information about dishes
 	 * @param orders: list holding information about orders
 	 * 
 	 */
	
	public void assignOrders(List<Employee> personnel, List<Dish> menu, List<OrderItem> orders){
		for (OrderItem o: orders){
			String empName = o.getEmpName();
			String dishName = o.getDishName();
			for (Employee e: personnel){
				if (e.getName().equals(empName)){
					e.addOrder(o);
					o.setOrderEmp(e);
				}
			}
			for (Dish d: menu){
				if (d.getDishName().equals(dishName)){
					d.addOrder(o);
					o.setOrderDish(d);
				}
			}
		}
	}
	
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
	@Override
	public int performFullDataLoad(List<Employee> personnel, List<Dish> menu,List<OrderItem> orders) {
		empLoader.load(empFile, "\t", true, 4, personnel);
		dishLoader.load(dishFile, "\t", true, 4, menu);
		orderLoader.load(orderFile, "\t", true, 5, orders);
		assignOrders(personnel, menu, orders);

		return 0;
	}

	

}//end class
