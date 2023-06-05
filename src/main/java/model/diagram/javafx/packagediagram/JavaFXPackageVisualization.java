package model.diagram.javafx.packagediagram;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.graph.*;

import java.util.Map;
import java.util.Set;

public class JavaFXPackageVisualization {

    private SmartGraphPanel<String, String> graphView;
    private final Map<Vertex, Set<Arc<Vertex>>> diagram;

    public JavaFXPackageVisualization(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        this.diagram = diagram;
    }

    public SmartGraphPanel<String, String> createGraphView() {
        Graph<String, String> graph = createGraph();
        graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
        setVertexCustomStyle();
        return graphView;
    }

    private Graph<String, String> createGraph() {
        Digraph<String, String> directedGraph = new DigraphEdgeList<>();
        iterateVertexes(directedGraph);
        iterateEdges(directedGraph);
        return directedGraph;
    }

    private void iterateVertexes(Digraph<String, String> directedGraph) {
        for (Vertex vertex: diagram.keySet()) {
            directedGraph.insertVertex(vertex.getName());
        }
    }

    private void iterateEdges(Digraph<String, String> directedGraph){
        for (Set<Arc<Vertex>> arcs : diagram.values()) {
            for (Arc<Vertex> arc: arcs) {
                if (arc.getArcType().equals(ArcType.AGGREGATION)) {
                    directedGraph.insertEdge(arc.getTargetVertex().getName(), arc.getSourceVertex().getName(),
                            arc.getTargetVertex().getName() + "_" + arc.getSourceVertex().getName() + "_" + arc.getArcType().toString());
                }else {
                    directedGraph.insertEdge(arc.getSourceVertex().getName(), arc.getTargetVertex().getName(),
                            arc.getSourceVertex().getName() + "_" +arc.getTargetVertex().getName() + "_" + arc.getArcType().toString());
                }
            }
        }
    }

    private void setVertexCustomStyle() {
        for (Vertex vertex: diagram.keySet()){
            if (vertex.getVertexType().equals(VertexType.INTERFACE)) {
                graphView.getStylableVertex(vertex.getName()).setStyleClass("vertexInterface");
            }else {
                graphView.getStylableVertex(vertex.getName()).setStyleClass("vertexPackage");
            }
        }
    }
}
