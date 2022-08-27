package view;

import controller.Controller;
import controller.DiagramControllerFactory;
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

    private static final int DIAGRAM_TYPE = 0;

    private MenuUtility(){
        throw new java.lang.UnsupportedOperationException("Not to be instantiated");
    }

    public static void openProject(MenuBar menuBar){
        File selectedDirectory = FileAndDirectoryUtility.chooseDirectory("Load the Project's Source Folder", menuBar);
        if (selectedDirectory != null) {
            try {
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
        DiagramControllerFactory diagramControllerFactory = new DiagramControllerFactory();
        Controller diagramController = diagramControllerFactory.getDiagramController(((MenuItem) event.getSource()).getText().split(" ")[DIAGRAM_TYPE]);
        File selectedFile = FileAndDirectoryUtility.loadFile(String.format("Load %s", ((MenuItem) event.getSource()).getText()), menuBar);
        if (selectedFile != null) {
            DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
            diagramVisualization.setDiagramController(diagramController);
            diagramController.loadDiagram(selectedFile.toPath());
            diagramVisualization.loadDiagramVisualization(diagramController.visualizeJavaFXGraph(), "loaded");
        }
    }
}
