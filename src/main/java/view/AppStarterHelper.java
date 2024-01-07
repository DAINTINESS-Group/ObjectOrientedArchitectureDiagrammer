package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AppStarterHelper extends Application
{

    public static void main(String[] args)
    {
        launch();
    }


    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            URL    url       = getClass().getResource("/fxml/ProjectLoadView.fxml");
            Parent root      = FXMLLoader.load(url);
            Scene  scene     = new Scene(root);
            Image  iconImage = new Image(getClass().getResourceAsStream("/assets/logo.png"));
            primaryStage.getIcons().add(iconImage);
            primaryStage.setTitle("Object Oriented Architecture Diagrammer");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(1);
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}