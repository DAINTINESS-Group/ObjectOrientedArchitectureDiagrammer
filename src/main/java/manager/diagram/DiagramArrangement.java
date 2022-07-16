package manager.diagram;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.LeafNode;
import model.Relationship;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiagramArrangement {
    private final Map<Integer, List<Double>> nodesGeometry;

    public DiagramArrangement() {
        nodesGeometry = new HashMap<>();
    }

    public void arrangeDiagram(Map<LeafNode, Integer> graphNodes, Map<Relationship, Integer> graphEdges) {
        Graph<Integer, String> graph = new SparseGraph<>();
        addVertexes(graphNodes, graph);
        addEdges(graphNodes, graphEdges, graph);
        AbstractLayout<Integer, String> layout = new SpringLayout(graph);
        layout.setSize(new Dimension(1500, 1000));
        populateNodesGeometry(layout, graphNodes);
    }

    private void addVertexes(Map<LeafNode, Integer> graphNodes, Graph<Integer, String> graph) {
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }
    }

    private void addEdges(Map<LeafNode, Integer> graphNodes, Map<Relationship, Integer> graphEdges, Graph<Integer, String> graph) {
        for (Map.Entry<Relationship, Integer> entry : graphEdges.entrySet()) {
            graph.addEdge(graphNodes.get(entry.getKey().getStartingLeafNode()) + " " + graphNodes.get(entry.getKey().getEndingLeafNode()),
                    graphNodes.get(entry.getKey().getStartingLeafNode()), graphNodes.get(entry.getKey().getEndingLeafNode()), EdgeType.DIRECTED);
        }
    }

    private void populateNodesGeometry(AbstractLayout<Integer, String> layout, Map<LeafNode, Integer> graphNodes) {
        for (Integer i : graphNodes.values()) {
            nodesGeometry.put(i, Arrays.asList(layout.getX(i), layout.getY(i)));
        }
    }
    public Map<Integer, List<Double>> getNodesGeometry() {
        return nodesGeometry;
    }
}