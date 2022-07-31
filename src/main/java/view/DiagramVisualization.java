package view;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class DiagramVisualization {

    @FXML
    MenuBar menuBar;

    private Controller diagramController;

    public DiagramVisualization(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public void loadDiagramVisualization(Map<String, Map<String, String>> graph) {
        try {
            URL url = getClass().getResource("/fxml/DiagramVisualizationView.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);
            Parent diagramVisualizationParent = loader.load();

            DiagramVisualizationController diagramVisualizationController = loader.getController();
            diagramVisualizationController.setDiagramController(diagramController);
            diagramVisualizationController.visualizeGraph(graph);

            Scene diagramVisualizationScene = new Scene(diagramVisualizationParent);
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(diagramVisualizationScene);
            window.show();

            diagramVisualizationController.getGraphView().init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDiagramController(Controller diagramController) {
        this.diagramController = diagramController;
    }
}
