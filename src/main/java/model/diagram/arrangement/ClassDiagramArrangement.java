package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.javatuples.Pair;

import java.awt.*;
import java.util.*;

public class ClassDiagramArrangement implements DiagramArrangement{
    private final Map<Integer, Pair<Double, Double>> nodesGeometry;
    private final Map<SinkVertex, Integer> graphNodes;
    private final Map<Arc<SinkVertex>, Integer> graphEdges;
    private final Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;

    public ClassDiagramArrangement(Map<SinkVertex, Integer> graphNodes, Map<Arc<SinkVertex>, Integer> graphEdges, Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        this.graphNodes = graphNodes;
        this.graphEdges = graphEdges;
        this.diagram = diagram;
        nodesGeometry = new HashMap<>();
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram() {
        Graph<Integer, String> graph = createGraph();
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        for (Integer i : graphNodes.values()) {
            nodesGeometry.put(i, new Pair<>(layout.getX(i), layout.getY(i)));
        }
        return nodesGeometry;
    }

    private Graph<Integer, String> createGraph(){
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer nodeId: graphNodes.values()) {
            graph.addVertex(nodeId);
        }

        for (Arc<SinkVertex> arc : graphEdges.keySet()) {
            Optional<Arc<SinkVertex>> optionalArc = diagram.get(arc.getSourceVertex()).stream()
                .filter(sinkVertexArc ->
                    sinkVertexArc.getTargetVertex().equals(arc.getTargetVertex()) &&
                    sinkVertexArc.getArcType().equals(arc.getArcType()))
                .findFirst();
            if (optionalArc.isEmpty()) {
                continue;
            }
            graph.addEdge(graphNodes.get(arc.getSourceVertex()) + " " + graphNodes.get(arc.getTargetVertex()),
                graphNodes.get(arc.getSourceVertex()), graphNodes.get(arc.getTargetVertex()), EdgeType.DIRECTED);
        }
        return graph;
    }

}