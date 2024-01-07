package bookstore;

import java.util.Objects;

/**
 * <h1>Item</h1>
 * Abstract class responsible for handling all the different types of items.
 *
 * @version 1.0
 * @since 2017-07-17
 */
public abstract class Item
{
    protected final String title;
    protected final double price;
    protected final int    id;


    public Item() {title = ""; price = -1.0; id = -1;}


    public Item(String aTitle, double aPrice, int id)
    {
        title = aTitle; price = aPrice; this.id = id;
    }


    /**
     * Shows the information for an item in detail
     *
     * @return a String with a detailed description
     */
    public abstract String getDescriptionInDetail();

    /**
     * Shows the information for an item concisely
     *
     * @return a String with the information for an item in short
     */
    public abstract String getDescriptionShort();

    /**
     * Returns the price of an Item after discounts to the original price
     *
     * @return a double the price after discount of a selected item
     */
    public abstract double getFinalPrice();


    /**
     * @return the title of an item
     */
    public String getTitle() {return title;}


    /**
     * @return the id of an item
     */
    public int getId() {return id;}


    /**
     * @return the original price of a selected item
     */
    public double getOriginalPrice() {return price;}

}

