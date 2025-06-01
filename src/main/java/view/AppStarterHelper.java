package view;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppStarterHelper extends Application {

    private static final Logger logger = LogManager.getLogger(AppStarterHelper.class);

    private static final String PROJECT_LOAD_VIEW = "/fxml/ProjectLoadView.fxml";
    private static final String LOGO_PNG = "/assets/logo.png";

    public static void main(String... args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            URL url = getClass().getResource(PROJECT_LOAD_VIEW);
            Parent root = FXMLLoader.load(Objects.requireNonNull(url));
            Scene scene = new Scene(root);
            Image iconImage =
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream(LOGO_PNG)));
            primaryStage.getIcons().add(iconImage);
            primaryStage.setTitle("Object Oriented Architecture Diagrammer");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setOnCloseRequest(
                    __ -> {
                        Platform.exit();
                        System.exit(1);
                    });
        } catch (IOException e) {
            logger.error("Failed to load {}", PROJECT_LOAD_VIEW);
            throw new RuntimeException(e);
        }
    }
}
