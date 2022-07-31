package view;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.util.Map;


public class DiagramVisualizationController {

    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;

    private SmartGraphPanel<String, String> graphView;
    private Controller diagramController;

    public void visualizeGraph(Map<String, Map<String, String>> diagram) {
        JavaFXGraphVisualization javaFXGraph = new JavaFXGraphVisualization(diagram);
        graphView = javaFXGraph.createGraphView();
        javaFXGraph.addGraphActions(menuBar);
        borderPane.setCenter(new ContentZoomPane(graphView));
    }

    public SmartGraphPanel<String, String> getGraphView() {
        return graphView;
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

    public void saveDiagram() {
        File selectedFile = FileAndDirectoryUtility.saveFile("Save Diagram", menuBar, "Text Files");
        if (selectedFile != null) {
            diagramController.saveDiagram(selectedFile.getPath());
        }
    }

    public void loadDiagram() {
        PopupWindow popupWindow = new PopupWindow(menuBar);
        popupWindow.createPopupInfoWindow("Close the current diagram first!", "Error");
    }

    public void setDiagramController(Controller diagramController) {
        this.diagramController = diagramController;
    }
}
