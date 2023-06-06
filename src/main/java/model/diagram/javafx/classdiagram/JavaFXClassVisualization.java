package model.diagram.javafx.classdiagram;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.SinkVertex;
import model.graph.VertexType;

import java.util.Map;
import java.util.Set;

public class JavaFXClassVisualization implements model.diagram.javafx.JavaFXVisualization {

    private SmartGraphPanel<String, String> graphView;
    private final Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;

    public JavaFXClassVisualization(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        this.diagram = diagram;
    }

    @Override
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
        for (SinkVertex sinkVertex: diagram.keySet()) {
            directedGraph.insertVertex(sinkVertex.getName());
        }
    }

    private void iterateEdges(Digraph<String, String> directedGraph){
        for (Set<Arc<SinkVertex>> arcs : diagram.values()) {
            for (Arc<SinkVertex> arc: arcs) {
                if (arc.getArcType().equals(ArcType.AGGREGATION)) {
                    directedGraph.insertEdge(arc.getTargetVertex().getName(), arc.getSourceVertex().getName(),
                            arc.getTargetVertex().getName() + "_" + arc.getSourceVertex().getName() + "_" + arc.getArcType().toString());
                }else {
                    directedGraph.insertEdge(arc.getSourceVertex().getName(), arc.getTargetVertex().getName(),
                            arc.getSourceVertex().getName() + "_" + arc.getTargetVertex().getName() + "_" + arc.getArcType().toString());
                }
            }
        }
    }

    private void setVertexCustomStyle() {
        for (SinkVertex sinkVertex: diagram.keySet()){
            if (sinkVertex.getVertexType().equals(VertexType.INTERFACE)) {
                graphView.getStylableVertex(sinkVertex.getName()).setStyleClass("vertexInterface");
            }else {
                graphView.getStylableVertex(sinkVertex.getName()).setStyleClass("vertexPackage");
            }
        }
    }

}
