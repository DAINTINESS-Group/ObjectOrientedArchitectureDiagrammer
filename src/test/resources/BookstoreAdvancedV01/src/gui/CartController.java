package gui;

import bookstore.Item;
import bookstore.ShoppingCart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * <h1>CartController</h1>
 * Responsible for displaying the cart information. In addition, provides the user the ability to delete any of the items that are added to the cart.
 *
 * @version 1.0
 * @since 2017-07-17
 */

public class CartController
{
    @FXML
    private TableView<ShopItem>           cartItemTable;
    @FXML
    private TableColumn<ShopItem, String> cartItemTitle;
    @FXML
    private TableColumn<ShopItem, String> cartItemPrice;
    @FXML
    ObservableList<ShopItem> cartItems = FXCollections.observableArrayList();
    @FXML
    private Label totalCost = new Label();

    private ShoppingCart cart;


    public CartController(ShoppingCart cart)
    {
        this.cart = cart;
    }


    public void setCartItems()
    {
        cartItems.clear();
        for (Item item : cart.getItems())
        {
            cartItems.add(new ShopItem(item.getDescriptionShort(), item.getFinalPrice(), item.getId()));
        }

        cartItemTitle.setCellValueFactory(new PropertyValueFactory<ShopItem, String>("title"));
        cartItemPrice.setCellValueFactory(new PropertyValueFactory<ShopItem, String>("price"));
        cartItemTable.getItems().setAll(cartItems);
        totalCost.setText("Total Cost: " + cart.getTotalCost());
    }


    @FXML
    public void removeFromCart()
    {
        ShopItem selectedItem = cartItemTable.getSelectionModel().getSelectedItem();
        cart.removeItem(selectedItem.getId());

        this.setCartItems();
    }
}
