package view;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import controller.Controller;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.MenuBar;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DiagramVisualizationController {

    private static final int EDGE_STARTING_NODE = 0;
    private static final int EDGE_ENDING_NODE = 1;
    private static final int EDGE_TYPE = 2;

    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;

    private SmartGraphPanel<String, String> graphView;
    private Controller diagramController;

    public void visualizeGraph(SmartGraphPanel<String, String> graphView) {
        this.graphView = graphView;
        addGraphActions();
        borderPane.setCenter(new ContentZoomPane(graphView));
    }

    public void addGraphActions() {
        addVertexActions();
        addEdgeActions();
    }

    private void addVertexActions() {
        graphView.setVertexDoubleClickAction((graphVertex) -> {
            PopupWindow popupWindow = new PopupWindow(menuBar);
            popupWindow.createPopupInfoWindow(String.format("Vertex contains element: %s", graphVertex.getUnderlyingVertex().element()),
                    "Node Information");
        });
    }

    private void addEdgeActions() {
        graphView.setEdgeDoubleClickAction((graphEdge) -> {
            PopupWindow popupWindow = new PopupWindow(menuBar);
            popupWindow.createPopupInfoWindow(String.format("Edge starting node: %s", graphEdge.getUnderlyingEdge().element().split("_")[EDGE_STARTING_NODE]) +
                    "\n" + String.format("Edge ending node: %s", graphEdge.getUnderlyingEdge().element().split("_")[EDGE_ENDING_NODE]) +
                    "\n" + String.format("Type of relationship: %s", Character.toUpperCase(graphEdge.getUnderlyingEdge().element().split("_")[EDGE_TYPE].charAt(0)) +
                    graphEdge.getUnderlyingEdge().element().split("_")[EDGE_TYPE].substring(1)), "Edge Information");
        });
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

    public void ExportDiagram() {
        File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram as PNG", menuBar,"PNG files");
        if (selectedDirectory != null) {
            WritableImage image = borderPane.snapshot(new SnapshotParameters(), null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedDirectory);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void aboutPage() { MenuUtility.aboutPage(menuBar); }

    public void setDiagramController(Controller diagramController) {
        this.diagramController = diagramController;
    }
}
