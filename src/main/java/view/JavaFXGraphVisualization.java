package view;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.scene.control.MenuBar;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JavaFXGraphVisualization {

    private static final int NODE_NAME = 0;
    private static final int NODE_TYPE = 1;
    private static final int EDGE_STARTING_NODE = 0;
    private static final int EDGE_ENDING_NODE = 1;
    private static final int EDGE_TYPE = 2;

    private final Map<String, Map<String, String>> diagram;
    private Digraph<String, String> directedGraph;
    private SmartGraphPanel<String, String> graphView;

    public JavaFXGraphVisualization(Map<String, Map<String, String>> diagram) {
        this.diagram = diagram;
    }

    public SmartGraphPanel<String, String> createGraphView() {
        Graph<String, String> graph = populateGraph(diagram);
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphView = new SmartGraphPanel<>(graph, strategy);
        setVertexCustomStyle();
        return graphView;
    }

    public void addGraphActions(MenuBar menuBar) {
        addVertexActions(menuBar);
        addEdgeActions(menuBar);
    }

    private Graph<String, String> populateGraph(Map<String, Map<String, String>> diagram) {
        directedGraph = new DigraphEdgeList<>();
        iterateVertexes(diagram);
        iterateEdges(diagram);
        return directedGraph;
    }

    private void setVertexCustomStyle() {
        for (String packageName: diagram.keySet()) {
            if (packageName.split("_")[NODE_TYPE].equals("INTERFACE")) {
                graphView.getStylableVertex(packageName.split("_")[NODE_NAME]).setStyleClass("vertexInterface");
            }
        }
    }

    private void iterateVertexes(Map<String, Map<String, String>> graph) {
        for (String vertex: graph.keySet()) {
            directedGraph.insertVertex(vertex.split("_")[NODE_NAME]);
        }
    }

    private void iterateEdges(Map<String, Map<String, String>> diagram){
        for(Map.Entry<String, Map<String, String>> vertexEntry: diagram.entrySet()) {
            for (Map.Entry<String, String> edgeEntry: vertexEntry.getValue().entrySet()) {
                if (edgeEntry.getValue().equals("AGGREGATION")) {
                    generateEdge(Arrays.asList(edgeEntry.getKey(), vertexEntry.getKey(), edgeEntry.getValue().toLowerCase()));
                }else {
                    generateEdge(Arrays.asList(vertexEntry.getKey(), edgeEntry.getKey(), edgeEntry.getValue().toLowerCase()));
                }
            }
        }
    }

    private void generateEdge(List<String> edgeInfo){
        directedGraph.insertEdge(edgeInfo.get(EDGE_STARTING_NODE).split("_")[NODE_NAME], edgeInfo.get(EDGE_ENDING_NODE).split("_")[NODE_NAME],
                edgeInfo.get(EDGE_STARTING_NODE).split("_")[NODE_NAME] + "_" + edgeInfo.get(EDGE_ENDING_NODE).split("_")[NODE_NAME] + "_" + edgeInfo.get(EDGE_TYPE));
    }

    private void addVertexActions(MenuBar menuBar) {
        graphView.setVertexDoubleClickAction((graphVertex) -> {
            PopupWindow popupWindow = new PopupWindow(menuBar);
            popupWindow.createPopupInfoWindow(String.format("Vertex contains element: %s", graphVertex.getUnderlyingVertex().element()),
                    "Node Information");
        });
    }

    private void addEdgeActions(MenuBar menuBar) {
        graphView.setEdgeDoubleClickAction((graphEdge) -> {
            PopupWindow popupWindow = new PopupWindow(menuBar);
            popupWindow.createPopupInfoWindow(String.format("Edge starting node: %s", graphEdge.getUnderlyingEdge().element().split("_")[EDGE_STARTING_NODE]) +
                    "\n" + String.format("Edge ending node: %s", graphEdge.getUnderlyingEdge().element().split("_")[EDGE_ENDING_NODE]) +
                    "\n" + String.format("Type of relationship: %s", Character.toUpperCase(graphEdge.getUnderlyingEdge().element().split("_")[EDGE_TYPE].charAt(0)) +
                    graphEdge.getUnderlyingEdge().element().split("_")[EDGE_TYPE].substring(1)), "Edge Information");
        });
    }

}
