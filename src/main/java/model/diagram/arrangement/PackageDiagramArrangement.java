package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.graph.Arc;
import model.graph.Vertex;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PackageDiagramArrangement implements DiagramArrangement {

    private final Map<Integer, List<Double>> nodesGeometry;
    private final Map<Vertex, Integer> graphNodes;
    private final Map<Arc<Vertex>, Integer> graphEdges;

    public PackageDiagramArrangement(Map<Vertex, Integer> graphNodes, Map<Arc<Vertex>, Integer> graphEdges) {
        this.graphNodes = graphNodes;
        this.graphEdges = graphEdges;
        nodesGeometry = new HashMap<>();
    }

    @Override
    public Map<Integer, List<Double>> arrangeDiagram() {
        Graph<Integer, String> graph = new SparseGraph<>();
        populatePackageGraph(graphNodes, graphEdges, graph);
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        populateNodesGeometry(layout, new ArrayList<>(graphNodes.values()));
        return nodesGeometry;
    }

    private void populatePackageGraph(Map<Vertex, Integer> graphNodes, Map<Arc<Vertex>, Integer> graphEdges, Graph<Integer, String> graph) {
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }

        for (Map.Entry<Arc<Vertex>, Integer> arc : graphEdges.entrySet()) {
            graph.addEdge(graphNodes.get(arc.getKey().getSourceVertex()) + " " + graphNodes.get(arc.getKey().getTargetVertex()),
                    graphNodes.get(arc.getKey().getSourceVertex()), graphNodes.get(arc.getKey().getTargetVertex()), EdgeType.DIRECTED);
        }
    }

    private void populateNodesGeometry(AbstractLayout<Integer, String> layout, List<Integer> graphNodesIds) {
        for (Integer i : graphNodesIds) {
            nodesGeometry.put(i, Arrays.asList(layout.getX(i), layout.getY(i)));
        }
    }
}
