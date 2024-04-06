package view;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;

public class DiagramVisualization
{
    private static final Logger logger = LogManager.getLogger(DiagramVisualization.class);

    private static final String DIAGRAM_VISUALIZATION_VIEW = "/fxml/DiagramVisualizationView.fxml";
    private static final String PROJECT_LOAD_VIEW          = "/fxml/ProjectLoadView.fxml";

    private static final int EDGE_STARTING_NODE = 0;
    private static final int EDGE_ENDING_NODE   = 1;
    private static final int EDGE_TYPE          = 2;

    @FXML
    MenuBar menuBar;

    private ProjectTreeView                 projectTreeView;
    private Controller                      diagramController;
    private SmartGraphPanel<String, String> graphView;


    public DiagramVisualization(MenuBar menuBar)
    {
        this.menuBar = menuBar;
    }


    public void loadDiagramVisualization(SmartGraphPanel<String, String> graphView)
    {
        this.graphView = graphView;
        try
        {
            URL        url    = getClass().getResource(DIAGRAM_VISUALIZATION_VIEW);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);
            Parent                         diagramVisualizationParent     = loader.load();
            DiagramVisualizationController diagramVisualizationController = loader.getController();
            diagramVisualizationController.setDiagramController(diagramController);
            diagramVisualizationController.setTreeView(projectTreeView);
            addGraphActions();
            diagramVisualizationController.visualizeGraph(graphView);
            Scene diagramVisualizationScene = new Scene(diagramVisualizationParent);
            Stage window                    = (Stage)menuBar.getScene().getWindow();
            window.setScene(diagramVisualizationScene);
            window.show();
            graphView.init();
            graphView = diagramVisualizationController.applyLayout();
            graphView.update();
        }
        catch (IOException e)
        {
            logger.error("Failed to load {}", DIAGRAM_VISUALIZATION_VIEW);
            throw new RuntimeException(e);
        }
    }


    public void loadLoadedDiagramVisualization(SmartGraphPanel<String, String> graphView)
    {
        this.graphView = graphView;
        try
        {
            URL        url    = getClass().getResource(PROJECT_LOAD_VIEW);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);

            Parent                parent                = loader.load();
            ProjectLoadController projectLoadController = loader.getController();
            projectLoadController.visualizeGraph(graphView);
            projectLoadController.setDiagramController(diagramController);
            Scene diagramVisualizationScene = new Scene(parent);
            Stage window                    = (Stage)menuBar.getScene().getWindow();
            window.setScene(diagramVisualizationScene);
            window.show();
            try
            {
                graphView = diagramController.visualizeLoadedJavaFXGraph();
                graphView.init();
                graphView.update();
            }
            catch (IllegalStateException e)
            {
                // Just continue. Handling here the exception to not show the error to the user.
                // We do this, because this error doesn't affect the system.
                logger.error("Failed to initialize Graph View.\n{}", e.toString());
            }
        }
        catch (IOException e)
        {
            logger.error("Failed to load {}", PROJECT_LOAD_VIEW);
            throw new RuntimeException(e);
        }
    }


    public void addGraphActions()
    {
        addVertexActions();
        addEdgeActions();
    }


    private void addVertexActions()
    {
        graphView.setVertexDoubleClickAction(it ->
             PopupWindow.createPopupInfoWindow(String.format("Vertex contains element: %s",
                                                             it.getUnderlyingVertex().element()),
                                               "Node Information"));
    }


    private void addEdgeActions()
    {
        graphView.setEdgeDoubleClickAction(it ->
            PopupWindow.createPopupInfoWindow(String.format("Edge starting node: %s",
                                                            it.getUnderlyingEdge().element().split("_")[EDGE_STARTING_NODE]) +
                                                            "\n" + String.format("Edge ending node: %s",
                                                                                 it.getUnderlyingEdge().element().split("_")[EDGE_ENDING_NODE]) +
                                                                                 "\n" + String.format("Type of relationship: %s",
                                                                                                      Character.toUpperCase(it.getUnderlyingEdge().element().split("_")[EDGE_TYPE].charAt(0)) +
                                                                                                      it.getUnderlyingEdge().element().split("_")[EDGE_TYPE].substring(1)),
                                              "Edge Information"));
    }


    public void setDiagramController(Controller diagramController)
    {
        this.diagramController = diagramController;
    }


    public void setProjectTreeView(ProjectTreeView projectTreeView)
    {
        this.projectTreeView = projectTreeView;
    }
}
