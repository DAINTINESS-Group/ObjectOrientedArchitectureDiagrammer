package gui;

import java.io.IOException;

import bookstore.Item;
import bookstore.ItemFactory;
import bookstore.ItemManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Gui extends Application
{

    private ItemManager amazon      = new ItemManager();
    private ItemFactory itemFactory = new ItemFactory();


    public Gui()
    {
        Item item = itemFactory.createBook("Discours de la methode", 50.00, "Rene Descartes", 1637, 0, 0);
        amazon.addItem(item);
        item = itemFactory.createBook("The Meditations", 30.00, "Marcus Aurelius", 180, 1, 1); amazon.addItem(item);

        item = itemFactory.createBook("The Bacchae", 30.00, "Euripides", -405, 1, 2);
        amazon.addItem(item);
        item = itemFactory.createBook("The Trojan Women", 40.00, "Euripides", -415, 0, 3);
        amazon.addItem(item);

        item = itemFactory.createCD("Piece of Mind", 10.0, "Iron Maiden", 4.0, 4);
        amazon.addItem(item);
        item = itemFactory.createCD("Matter of Life and Death", 12.0, "Iron Maiden", 2.0, 5);
        amazon.addItem(item);
        item = itemFactory.createCD("Perfect Strangers", 12.00, "Deep Purple", 1.0, 6);
        amazon.addItem(item);
    }


    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setResizable(false);
        FXMLLoader      loader  = new FXMLLoader(getClass().getResource("/gui/gui.fxml"));
        GuiEventHandler handler = new GuiEventHandler(amazon.getAllItems());
        handler.setStage(primaryStage);
        loader.setController(handler);

        Pane mainPane = null;
        try
        {
            mainPane = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane);
        primaryStage.setTitle("Amazon");
        primaryStage.setScene(scene);
        primaryStage.show();
        handler.setItems();
    }


    public static void main(String[] args)
    {

        Gui gui = new Gui();
        launch(args);
    }
}
