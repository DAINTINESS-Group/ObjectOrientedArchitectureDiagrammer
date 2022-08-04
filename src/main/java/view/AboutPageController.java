package view;

import controller.Controller;
import controller.DiagramControllerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutPageController implements Initializable {

    private static final int DIAGRAM_TYPE = 0;

    @FXML
    MenuBar menuBar;
    @FXML
    TextFlow textFlow;

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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Text text1 = new Text("Sample text");
        textFlow.getChildren().add(text1);
    }
}
