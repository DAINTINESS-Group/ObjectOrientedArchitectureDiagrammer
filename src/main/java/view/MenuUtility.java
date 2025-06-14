package view;

import com.google.gson.JsonParseException;
import controller.Controller;
import controller.ControllerFactory;
import java.io.File;
import java.net.URL;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MenuUtility {

    private MenuUtility() {
        throw new java.lang.UnsupportedOperationException("Not to be instantiated");
    }

    public static void openProject(MenuBar menuBar) {
        try {
            File selectedDirectory =
                    FileUtility.chooseDirectory("Load the Project's Source Folder", menuBar);
            if (selectedDirectory == null) {
                PopupWindow.createPopupInfoWindow("You should select a directory!", "Error");
                return;
            }
            URL url = MenuUtility.class.getResource("/fxml/DiagramCreationView.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);
            Parent diagramCreationParent = loader.load();

            DiagramCreationController diagramCreationController = loader.getController();
            diagramCreationController.createTreeView(selectedDirectory.toPath());

            Scene diagramCreationScene = new Scene(diagramCreationParent);
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(diagramCreationScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeProject(MenuBar menuBar) {
        try {
            URL url = MenuUtility.class.getResource("/fxml/ProjectLoadView.fxml");
            Parent diagramCreationParent = FXMLLoader.load(Objects.requireNonNull(url));
            Scene diagramCreationScene = new Scene(diagramCreationParent);
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(diagramCreationScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void aboutPage(MenuBar menuBar) {
        try {
            URL url = MenuUtility.class.getResource("/fxml/AboutPageView.fxml");
            Parent aboutPageParent = FXMLLoader.load(Objects.requireNonNull(url));
            Scene diagramCreationScene = new Scene(aboutPageParent);
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(diagramCreationScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void quitApp(MenuBar menuBar) {
        Stage window = (Stage) menuBar.getScene().getWindow();
        window.close();
    }

    public static String loadDiagram(MenuBar menuBar, ActionEvent event) {
        Controller diagramController =
                ControllerFactory.createController("uml", ((MenuItem) event.getSource()).getText());
        File selectedFile =
                FileUtility.loadFile(
                        String.format("Load %s Diagram", ((MenuItem) event.getSource()).getText()),
                        menuBar);
        if (selectedFile == null) {
            return null;
        }
        try {
            DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
            diagramVisualization.setDiagramController(diagramController);
            diagramController.loadDiagram(selectedFile.toPath());
            diagramVisualization.loadLoadedDiagramVisualization(
                    diagramController.visualizeLoadedJavaFXGraph());
        } catch (JsonParseException j) {
            if (j.getMessage().equals("Wrong diagram type")) {
                PopupWindow.createPopupInfoWindow(
                        "You tried to load the wrong type of diagram", "Error");
            } else {
                PopupWindow.createPopupInfoWindow("Unsupported type of file", "Error");
            }
        }
        return selectedFile.getName();
    }
}
