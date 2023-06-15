package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.graph.Arc;
import model.graph.Vertex;
import org.javatuples.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PackageDiagramArrangement implements DiagramArrangement {

    private final Map<Integer, Pair<Double, Double>> nodesGeometry;
    private final Map<Vertex, Integer> graphNodes;
    private final Map<Vertex, Set<Arc<Vertex>>> diagram;

    public PackageDiagramArrangement(Map<Vertex, Integer> graphNodes, Map<Vertex, Set<Arc<Vertex>>> diagram) {
        this.graphNodes = graphNodes;
        this.diagram = diagram;
        nodesGeometry = new HashMap<>();
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram() {
        Graph<Integer, String> graph = populatePackageGraph(graphNodes);
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        for (Integer i : graphNodes.values()) {
            nodesGeometry.put(i, new Pair<>(layout.getX(i), layout.getY(i)));
        }
        return nodesGeometry;
    }

    private Graph<Integer, String> populatePackageGraph(Map<Vertex, Integer> graphNodes) {
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }

        List<Arc<Vertex>> arcs = new ArrayList<>();
        for (Set<Arc<Vertex>> arcSet: diagram.values()) {
            arcs.addAll(arcSet);
        }

        for (Arc<Vertex> arc: arcs) {
            graph.addEdge(graphNodes.get(arc.getSourceVertex()) + " " + graphNodes.get(arc.getTargetVertex()),
                graphNodes.get(arc.getSourceVertex()), graphNodes.get(arc.getTargetVertex()), EdgeType.DIRECTED);
        }

        return graph;
    }

}
