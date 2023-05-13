package gui;
import java.util.ArrayList;
import java.util.List;

import bookstore.Item;
import bookstore.ShoppingCart;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class GuiEventHandler {
    @FXML private Button additionButton;
	
	@FXML private TableView<ShopItem> tableItems;
    @FXML private TableColumn<ShopItem, String> title;
    @FXML private TableColumn<ShopItem, String> price;
    @FXML
	ObservableList<ShopItem> items = FXCollections.observableArrayList();
    
    
	private Stage stage;
	private ShoppingCart cart;
	private List<Item> originalItems;
	
	public GuiEventHandler(List<Item> items){
		for(Item item:items){
			this.items.add(new ShopItem(item.getDescriptionShort(), item.getFinalPrice(), item.getId()));
		}
		System.out.println(items.size());
		cart = new ShoppingCart();
		originalItems = new ArrayList<Item>();
		originalItems = items;
	}
	
	public void setItems(){
		title.setCellValueFactory(new PropertyValueFactory<ShopItem, String>("title"));
		price.setCellValueFactory(new PropertyValueFactory<ShopItem, String>("price"));
		tableItems.getItems().setAll(items);
	}
	
	@FXML
	 private void addItem(ActionEvent event) {
		cart.addItem(originalItems.get(tableItems.getSelectionModel().getSelectedIndex()));
	 }
	
	
	
	@FXML
	public void showCart(){
		CartViewer cart = new CartViewer(stage,this.cart);
	}
	
	
	@FXML
	public void exit(){
		Platform.exit();
	}

	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
