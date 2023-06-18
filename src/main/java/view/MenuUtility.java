package view;

import com.google.gson.JsonParseException;
import controller.Controller;
import controller.DiagramController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class MenuUtility {

    private MenuUtility(){
        throw new java.lang.UnsupportedOperationException("Not to be instantiated");
    }

    public static void openProject(MenuBar menuBar){
        try {
            File selectedDirectory = FileAndDirectoryUtility.chooseDirectory("Load the Project's Source Folder", menuBar);
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
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void closeProject(MenuBar menuBar){
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

    public static void quitApp(MenuBar menuBar){
        Stage window = (Stage) menuBar.getScene().getWindow();
        window.close();
    }

    public static void loadDiagram(MenuBar menuBar, ActionEvent event) {
        Controller diagramController = new DiagramController(((MenuItem) event.getSource()).getText());
        File selectedFile = FileAndDirectoryUtility.loadFile(String.format("Load %s Diagram", ((MenuItem) event.getSource()).getText()), menuBar);
        if (selectedFile == null) {
            return;
        }
        try {
            DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
            diagramVisualization.setDiagramController(diagramController);
            diagramController.loadDiagram(selectedFile.toPath());
            diagramVisualization.loadLoadedDiagramVisualization(diagramController.visualizeJavaFXGraph());
        } catch (JsonParseException j) {
            if (j.getMessage().equals("Wrong diagram type")) {
                PopupWindow.createPopupInfoWindow("You tried to load the wrong type of diagram", "error");
            } else {
                j.printStackTrace();
            }
        }
    }
}
