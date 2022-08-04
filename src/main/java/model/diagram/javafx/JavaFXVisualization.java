package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JavaFXVisualization {

    private static final int NODE_NAME = 0;
    private static final int EDGE_STARTING_NODE = 0;
    private static final int EDGE_ENDING_NODE = 1;
    private static final int NODE_TYPE = 1;
    private static final int EDGE_TYPE = 2;

    private SmartGraphPanel<String, String> graphView;

    public SmartGraphPanel<String, String> createGraphView(Map<String, Map<String, String>> diagram) {
        Graph<String, String> graph = createGraph(diagram);
        graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
        setVertexCustomStyle(diagram);
        return graphView;
    }

    private Graph<String, String> createGraph(Map<String, Map<String, String>> diagram) {
        Digraph<String, String> directedGraph = new DigraphEdgeList<>();
        iterateVertexes(diagram, directedGraph);
        iterateEdges(diagram, directedGraph);
        return directedGraph;
    }

    private void setVertexCustomStyle(Map<String, Map<String, String>> diagram) {
        for (String packageName: diagram.keySet()) {
            if (packageName.split("_")[NODE_TYPE].equals("INTERFACE")) {
                graphView.getStylableVertex(packageName.split("_")[NODE_NAME]).setStyleClass("vertexInterface");
            } else if (packageName.split("_")[NODE_TYPE].equals("PACKAGE")) {
                graphView.getStylableVertex(packageName.split("_")[NODE_NAME]).setStyleClass("vertexPackage");
            }
        }
    }

    private void iterateVertexes(Map<String, Map<String, String>> graph, Digraph<String, String> directedGraph) {
        for (String vertex: graph.keySet()) {
            directedGraph.insertVertex(vertex.split("_")[NODE_NAME]);
        }
    }

    private void iterateEdges(Map<String, Map<String, String>> diagram, Digraph<String, String> directedGraph){
        for(Map.Entry<String, Map<String, String>> vertexEntry: diagram.entrySet()) {
            for (Map.Entry<String, String> edgeEntry: vertexEntry.getValue().entrySet()) {
                if (edgeEntry.getValue().equals("AGGREGATION")) {
                    generateEdge(Arrays.asList(edgeEntry.getKey(), vertexEntry.getKey(), edgeEntry.getValue().toLowerCase()),
                            directedGraph);
                }else {
                    generateEdge(Arrays.asList(vertexEntry.getKey(), edgeEntry.getKey(), edgeEntry.getValue().toLowerCase()),
                            directedGraph);
                }
            }
        }
    }

    private void generateEdge(List<String> edgeInfo, Digraph<String, String> directedGraph){
        directedGraph.insertEdge(edgeInfo.get(EDGE_STARTING_NODE).split("_")[NODE_NAME], edgeInfo.get(EDGE_ENDING_NODE).split("_")[NODE_NAME],
                edgeInfo.get(EDGE_STARTING_NODE).split("_")[NODE_NAME] + "_" + edgeInfo.get(EDGE_ENDING_NODE).split("_")[NODE_NAME] +
                        "_" + edgeInfo.get(EDGE_TYPE));
    }

}
