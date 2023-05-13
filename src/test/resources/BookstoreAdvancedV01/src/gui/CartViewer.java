package gui;

import java.io.IOException;

import bookstore.ShoppingCart;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
* <h1>CartViewer</h1>
* Responsible for initializing the main window which
* provides the interaction with the user regarding the
* cart information.
*
* @version 1.0
* @since   2017-07-17
*/
public class CartViewer {
   
	public CartViewer(Stage stage, ShoppingCart cart){
		init(stage,cart);		
	}
	
	public CartViewer(){}
	
	private void init(Stage stage, ShoppingCart cart){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/cart.fxml"));
		
		CartController handler = new CartController(cart);
		loader.setController(handler);
		
		Pane mainPane = null;
		
		try {
			mainPane = loader.load();
			Scene scene = new Scene(mainPane);
			final Stage dialog = new Stage();
	        dialog.initModality(Modality.WINDOW_MODAL);
	        dialog.initOwner(stage);
			dialog.setTitle("Cart");
			dialog.setScene(scene);
			dialog.setResizable(false);
			
		    dialog.show();
		    handler.setCartItems();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
