package bookstore;

import java.util.ArrayList;
import java.util.List;



/**
* <h1>ItemManager</h1>
* Responsible for handling all 
* the functionality needed for the items.
*
* @version 1.0
* @since   2017-07-17
*/
public final class ItemManager {
	private final List<Item> allItems;
	
	public ItemManager(){
		allItems = new ArrayList<Item>();
	}
	
	/**
 	 * Adds an item in the list
 	 * @param an Item to be added to the products for sale
 	 * @return an int with the size of the items' list
 	 */
	public int addItem(Item anItem){
		allItems.add(anItem);
		return allItems.size();
	}
	
	/**
 	 * Removes an item from the list
 	 * @return an int with the size of the items' list
 	 */
	public int removeItem(int index){
		this.allItems.remove(index);
		return allItems.size();
	}
	
	/**
 	 * Retrieves an item from a selected position
 	 * 
 	 * @param index: position of the item to be retrieved
 	 * @return an item from the list
 	 */
	public Item getItem(int index){
		return this.allItems.get(index);
	}
	
	/**
 	 * 
 	 * @return the list holding the item
 	 */
	public List<Item> getAllItems(){
		return this.allItems;
	}
	
	/**
 	 * Displays all the information for each item in the console
 	 */
	public String reportAllItems(){
		String result ="";
		for(Item item: this.allItems) {
			result = result + item.getDescriptionInDetail();
		}
		result = result + "Total number of items: " + this.allItems.size()+ "\n";
		System.out.println(result);
		return result;
	}
}

