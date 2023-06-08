package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.graph.Arc;
import model.graph.SinkVertex;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ClassDiagramArrangement implements DiagramArrangement{
    private final Map<Integer, List<Double>> nodesGeometry;
    private final Map<SinkVertex, Integer> graphNodes;
    private final Map<Arc<SinkVertex>, Integer> graphEdges;

    public ClassDiagramArrangement(Map<SinkVertex, Integer> graphNodes, Map<Arc<SinkVertex>, Integer> graphEdges) {
        this.graphNodes = graphNodes;
        this.graphEdges = graphEdges;
        nodesGeometry = new HashMap<>();
    }

    @Override
    public Map<Integer, List<Double>> arrangeDiagram() {
        Graph<Integer, String> graph = new SparseGraph<>();
        populateClassGraph(graph);
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        populateNodesGeometry(layout);
        return nodesGeometry;
    }

    private void populateClassGraph(Graph<Integer, String> graph){
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }
        for (Map.Entry<Arc<SinkVertex>, Integer> arc : graphEdges.entrySet()) {
            graph.addEdge(graphNodes.get(arc.getKey().getSourceVertex()) + " " + graphNodes.get(arc.getKey().getTargetVertex()),
                    graphNodes.get(arc.getKey().getSourceVertex()), graphNodes.get(arc.getKey().getTargetVertex()), EdgeType.DIRECTED);
        }
    }

    private void populateNodesGeometry(AbstractLayout<Integer, String> layout) {
        for (Integer i : graphNodes.values()) {
            nodesGeometry.put(i, Arrays.asList(layout.getX(i), layout.getY(i)));
        }
    }
}