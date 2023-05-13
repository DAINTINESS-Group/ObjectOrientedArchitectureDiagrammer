package bookstore;

import java.util.ArrayList;
import java.util.List;

/**
* <h1>Shopping Cart</h1>
* Class responsible for storing all 
* the items that are selected from the user.
*
* @version 1.0
* @since   2017-07-17
*/
public class ShoppingCart {
	
	private final List<Item> items;
	private float totalCost;
	
	public ShoppingCart(){
		this.items = new ArrayList<Item>();
		this.totalCost = 0;
	}
	
	/**
 	 * Adds an item to the cart
 	 * 
 	 * @param item: item for addition
 	 * @return an int with the number of the items in the cart
 	 */
	public int addItem(Item item){
		this.items.add(item);
		return this.items.size();
	}
	
	/**
 	 * Removes the item in the selected position
 	 * 
 	 * Returns a diagnostic message in case of wrong item
 	 * @param anId: id of the item for deletion
 	 * @return an int with the number of the items in the cart
 	 */
	public int removeItem(int anId){
		int pos = -1;
		for(int i = 0; i < this.items.size(); i++){
			if (items.get(i).getId() == anId){
				pos = i;
				break;
			}
		}
		if (pos>=0)
			this.items.remove(pos);
		else{
			System.out.println("The id you have specified is not in the cart");
		}
		return this.items.size();
	}
	
	private void computeTotalCost(){
		float cost = 0;
		for(Item item: this.items){
			cost += item.getFinalPrice();
		}
		this.totalCost = cost;
	}
	
	/**
 	 * @return the total cost of selected items
 	 */
	public float getTotalCost(){
		this.computeTotalCost();
		return this.totalCost;
	}
	
	/**
 	 * @return the list of all items which are
 	 * added to the cart
 	 */
	public List<Item> getItems(){
		return this.items;
	}
	
	/**
 	 * Displays the details of each item in the
 	 * cart in the console.
 	 */
	public String showDetails(){
		String intro = "------------------------\n" +
		               "       CART ITEMS       \n" +
		               "------------------------\n";
		
		String itemsString = "";
		for(int i = 0; i < this.items.size(); i++){
			//System.out.println("***Item id: " + i + "***");
			itemsString = itemsString + items.get(i).getDescriptionInDetail();
		}
		String costString = "Total cost: " + this.getTotalCost() + "\n";
		
		String result = intro + itemsString + costString;
		System.out.println(result);
		
		return result;
	}
}
