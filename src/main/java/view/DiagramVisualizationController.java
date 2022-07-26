package view;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class DiagramVisualizationController {

    private static final int CLASS_INTERFACE_NAME = 0;
    private static final int CLASS_INTERFACE_TYPE = 1;
    private static final int EDGE_STARTING_NODE = 0;
    private static final int EDGE_ENDING_NODE = 1;
    private static final int EDGE_TYPE = 2;

    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;
    @FXML

    private SmartGraphPanel<String, String> graphView;
    private Digraph<String, String> directedGraph;

    public void visualizeGraph(Map<String, Map<String, String>> graph, String diagramType) {

        com.brunomnsilva.smartgraph.graph.Graph<String, String> g = populateGraph(graph, diagramType);
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphView = new SmartGraphPanel(g, strategy);

        if (diagramType.equals("Package")) {
            for (String packageName: graph.keySet()) {
                graphView.getStylableVertex(packageName).setStyleClass("vertexPackage");
            }
        }else {
            for (String packageName: graph.keySet()) {
                if (packageName.split("_")[CLASS_INTERFACE_TYPE].equals("INTERFACE")) {
                    graphView.getStylableVertex(packageName.split("_")[CLASS_INTERFACE_NAME]).setStyleClass("vertexInterface");
                }

            }
        }

        borderPane.setCenter(new ContentZoomPane(graphView));
    }

    private Graph<String, String> populateGraph(Map<String, Map<String, String>> graph, String diagramType) {
        directedGraph = new DigraphEdgeList();

        generateVertexes(graph, diagramType);
        generateEdges(graph, diagramType);

        return directedGraph;
    }

    private void generateVertexes(Map<String, Map<String, String>> graph, String diagramType) {
        for (String vertex: graph.keySet()) {
            if (diagramType.equals("Class")) {
                directedGraph.insertVertex(vertex.split("_")[CLASS_INTERFACE_NAME]);
            }else {
                directedGraph.insertVertex(vertex);
            }
        }
    }

    private void generateEdges(Map<String, Map<String, String>> graph, String diagramType){
        for(Map.Entry<String, Map<String, String>> vertexEntry: graph.entrySet()) {
            for (Map.Entry<String, String> edgeEntry: vertexEntry.getValue().entrySet()) {
                if (edgeEntry.getValue().equals("AGGREGATION")) {
                    addEdgeFinal(diagramType, Arrays.asList(edgeEntry.getKey(), vertexEntry.getKey(), edgeEntry.getValue().toLowerCase()));
                }else {
                    addEdgeFinal(diagramType, Arrays.asList(vertexEntry.getKey(), edgeEntry.getKey(), edgeEntry.getValue().toLowerCase()));
                }
            }
        }
    }

    private void addEdgeFinal(String diagramType, List<String> edgeInfo){
        if (diagramType.equals("Class")) {
            addEdge(Arrays.asList(edgeInfo.get(EDGE_STARTING_NODE).split("_")[CLASS_INTERFACE_NAME],
                    edgeInfo.get(EDGE_ENDING_NODE).split("_")[CLASS_INTERFACE_NAME], edgeInfo.get(EDGE_TYPE)));
        }else {
            addEdge(Arrays.asList(edgeInfo.get(EDGE_STARTING_NODE), edgeInfo.get(EDGE_ENDING_NODE), edgeInfo.get(EDGE_TYPE)));
        }
    }

    private void addEdge(List<String> edgeInfo) {
        directedGraph.insertEdge(edgeInfo.get(EDGE_STARTING_NODE), edgeInfo.get(EDGE_ENDING_NODE), edgeInfo.get(EDGE_STARTING_NODE)
                + edgeInfo.get(EDGE_ENDING_NODE) + "_" + edgeInfo.get(EDGE_TYPE));
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

}
