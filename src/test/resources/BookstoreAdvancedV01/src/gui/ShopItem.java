package gui;

import javafx.beans.property.SimpleStringProperty;


/**
 * <h1>ShopItem</h1>
 * Data structure class responsible from storing the information regarding an item from the store for displaying it in the javafx table.
 *
 * @version 1.0
 * @since 2017-07-17
 */
public class ShopItem
{
    private SimpleStringProperty title;
    private SimpleStringProperty price;
    private int                  id;


    public ShopItem(String title, double price, int id)
    {
        this.title = new SimpleStringProperty(title);
        this.price = new SimpleStringProperty(Double.toString(price));
        this.id = id;
    }


    public String getTitle()
    {
        return title.get();
    }


    public void setTitle(SimpleStringProperty title)
    {
        this.title = title;
    }


    public String getPrice()
    {
        return price.get();
    }


    public void setPrice(SimpleStringProperty price)
    {
        this.price = price;
    }


    public int getId()
    {
        return id;
    }


    public void setId(int id)
    {
        this.id = id;
    }
}
