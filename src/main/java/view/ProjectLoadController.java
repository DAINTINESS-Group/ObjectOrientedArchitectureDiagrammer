package view;

import controller.Controller;
import controller.DiagramControllerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.File;

public class ProjectLoadController {

    private static final int DIAGRAM_TYPE = 0;

    @FXML
    MenuBar menuBar;

    public void newProject() {
        MenuUtility.newProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void quitApp() {
        MenuUtility.quitApp(menuBar);
    }

    public void aboutPage() { MenuUtility.aboutPage(menuBar); }

    public void saveDiagram() {
        PopupWindow popupWindow = new PopupWindow(menuBar);
        popupWindow.createPopupInfoWindow("You haven't created a diagram yet!", "Error");
    }

    public void loadDiagram(ActionEvent event) {
        DiagramControllerFactory diagramControllerFactory = new DiagramControllerFactory();
        Controller diagramController = diagramControllerFactory.getDiagramController(((MenuItem) event.getSource()).getText().split(" ")[DIAGRAM_TYPE]);
        File selectedFile = FileAndDirectoryUtility.loadFile(String.format("Load %s", ((MenuItem) event.getSource()).getText()), menuBar);
        if (selectedFile != null) {
            DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
            diagramVisualization.setDiagramController(diagramController);
            diagramController.loadDiagram(selectedFile.getPath());
            diagramVisualization.loadDiagramVisualization(diagramController.visualizeJavaFXGraph());
        }
    }

}
