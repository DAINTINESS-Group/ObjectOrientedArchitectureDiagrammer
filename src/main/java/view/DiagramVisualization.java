package view;

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

    public DiagramVisualization(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public void createDiagramVisualization(Map<String, Map<String, String>> graph, String diagramType) throws IOException {
        URL url = getClass().getResource("/fxml/DiagramVisualizationView.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        Parent diagramVisualizationParent = loader.load();

        DiagramVisualizationController diagramVisualizationController = loader.getController();
        diagramVisualizationController.visualizeGraph(graph, diagramType);

        Scene diagramVisualizationScene = new Scene(diagramVisualizationParent);
        Stage window = (Stage) menuBar.getScene().getWindow();
        window.setScene(diagramVisualizationScene);
        window.show();

        diagramVisualizationController.getGraphView().init();
    }
}
