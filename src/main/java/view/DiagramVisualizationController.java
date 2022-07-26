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

import java.util.Map;


public class DiagramVisualizationController {

    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;
    @FXML

    private SmartGraphPanel<String, String> graphView;

    public void visualizeGraph(Map<String, Map<String, String>> graph) {

        com.brunomnsilva.smartgraph.graph.Graph<String, String> g = populateGraph(graph);
        // System.out.println(g);
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        graphView = new SmartGraphPanel(g, strategy);
        /*
        if (g.numVertices() > 0) {
            graphView.getStylableVertex("A").setStyle("-fx-fill: gold; -fx-stroke: brown;");
        }
        */
        borderPane.setCenter(new ContentZoomPane(graphView));
        // scrollPane.setContent(new ContentZoomPane(graphView));
        // scrollPane.setContent(new SmartGraphDemoContainer(graphView));
/*
        Scene scene = new Scene(new SmartGraphDemoContainer(graphView), 1024.0, 768.0);
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500.0);
        stage.setMinWidth(800.0);
        stage.setScene(scene);
        stage.show();

 */
        // graphView.init();
        graphView.setVertexDoubleClickAction((graphVertex) -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
            if (!graphVertex.removeStyleClass("myVertex")) {
                graphVertex.addStyleClass("myVertex");
            }

        });
        graphView.setEdgeDoubleClickAction((graphEdge) -> {
            System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
            graphEdge.getStylableArrow().setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
        });
    }
    private Graph<String, String> populateGraph(Map<String, Map<String, String>> graph) {
        Digraph<String, String> directedGraph = new DigraphEdgeList();

        for (String vertex: graph.keySet()) {
            directedGraph.insertVertex(vertex);
        }
        for(Map.Entry<String, Map<String, String>> vertexEntry: graph.entrySet()) {
            for (Map.Entry<String, String> edgeEntry: vertexEntry.getValue().entrySet()) {
                if (edgeEntry.getValue().equals("AGGREGATION")) {
                    directedGraph.insertEdge(edgeEntry.getKey(), vertexEntry.getKey(),
                            vertexEntry.getKey()+edgeEntry.getKey()+"_"+edgeEntry.getValue().toLowerCase());
                }else {
                    directedGraph.insertEdge(vertexEntry.getKey(), edgeEntry.getKey(),
                            vertexEntry.getKey()+edgeEntry.getKey()+"_"+edgeEntry.getValue().toLowerCase());
                }
            }
        }
        return directedGraph;
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
