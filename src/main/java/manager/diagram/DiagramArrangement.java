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
    public void arrangeDiagram(GraphMLNode graphMLNode, GraphMLEdge graphMLEdge) {
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer i : graphMLNode.getGraphMLNodes().values()) {
            graph.addVertex(i);
        }
        for (Map.Entry<Relationship, Integer> entry : graphMLEdge.getGraphMLEdges().entrySet()) {
            graph.addEdge(graphMLNode.getGraphMLNodes().get(entry.getKey().getStartingLeafNode()) + " " +
                            graphMLNode.getGraphMLNodes().get(entry.getKey().getEndingLeafNode()),
                    graphMLNode.getGraphMLNodes().get(entry.getKey().getStartingLeafNode()),
                    graphMLNode.getGraphMLNodes().get(entry.getKey().getEndingLeafNode()), EdgeType.DIRECTED);
        }
        AbstractLayout<Integer, String> layout = new SpringLayout(graph);
        layout.setSize(new Dimension(1500, 1000));
        populateNodesGeometry(layout, graphMLNode);
    }
    private void populateNodesGeometry(AbstractLayout<Integer, String> layout, GraphMLNode graphMLNode) {
        for (Integer i : graphMLNode.getGraphMLNodes().values()) {
            nodesGeometry.put(i, Arrays.asList(layout.getX(i), layout.getY(i)));
        }
    }
    public Map<Integer, List<Double>> getNodesGeometry() {
        return nodesGeometry;
    }
}