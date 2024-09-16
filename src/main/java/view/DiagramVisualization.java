package view;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.StringReader;
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

    private SmartGraphPanel<String, String> graphView;
    private ProjectTreeView                 projectTreeView;
    private Controller                      diagramController;


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
            Stage window                    = (Stage) menuBar.getScene().getWindow();
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


    // TODO: See how feasible it is to remove this hack.
    public void loadSvgDiagram()
    {
        java.awt.EventQueue.invokeLater(() ->
        {
            try
            {
                final JFrame frame = new JFrame();
                final JPanel panel = new JPanel(new BorderLayout());
                final JSVGCanvas svgCanvas = new JSVGCanvas();

                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.getContentPane().add(panel, BorderLayout.CENTER);

                Dimension screenSize   = Toolkit.getDefaultToolkit().getScreenSize();
                String svg = diagramController.visualizeSvgGraph(getDpi(screenSize));

                String                parser      = XMLResourceDescriptor.getXMLParserClassName();
                SAXSVGDocumentFactory factory     = new SAXSVGDocumentFactory(parser);
                SVGDocument           svgDocument = factory.createSVGDocument(null, new StringReader(svg));
                svgCanvas.setDocument(svgDocument);

                panel.add(svgCanvas, BorderLayout.CENTER);
                frame.setSize(screenSize.width, screenSize.height / 2);
                JScrollPane scrollPane = new JScrollPane(panel);
                frame.add(scrollPane, BorderLayout.CENTER);
                frame.setVisible(true);
            }
            catch (IOException e)
            {
                logger.error("Failed to create SVG document");
                throw new RuntimeException(e);
            }
        });
    }


    private static int getDpi(Dimension screenSize)
    {
        // Get the screen size.
        double    screenWidth  = screenSize.getWidth();
        double    screenHeight = screenSize.getHeight();
        // Calculate the DPI based on the screen size.
        int    screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
        double diagonalInches   = Math.sqrt(Math.pow(screenWidth, 2) + Math.pow(screenHeight, 2)) / screenResolution;
        // Set a maximum DPI of 30.
        return diagonalInches > 30 ? 30 : (int) diagonalInches;
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
            logger.error("Failed to load {}, for loaded diagram", PROJECT_LOAD_VIEW);
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
