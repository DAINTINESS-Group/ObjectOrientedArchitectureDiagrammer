package manager.diagram;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class DiagramArrangement {
    protected final Map<Integer, List<Double>> nodesGeometry;

    public DiagramArrangement() {
        nodesGeometry = new HashMap<>();
    }

    public void arrangeDiagram(Map<Object, Integer> graphNodes, Map<Object, Integer> graphEdges) {
        Graph<Integer, String> graph = new SparseGraph<>();
        populateGraph(graphNodes, graphEdges, graph);
        AbstractLayout<Integer, String> layout = new SpringLayout(graph);
        layout.setSize(new Dimension(1500, 1000));
        populateNodesGeometry(layout, new ArrayList<>(graphNodes.values()));
    }

    public abstract void populateGraph(Map<Object, Integer> graphNodes, Map<Object, Integer> graphEdges, Graph<Integer, String> graph);

    private void populateNodesGeometry(AbstractLayout<Integer, String> layout, List<Integer> graphNodesIds) {
        for (Integer i : graphNodesIds) {
            nodesGeometry.put(i, Arrays.asList(layout.getX(i), layout.getY(i)));
        }
    }
    public Map<Integer, List<Double>> getNodesGeometry() {
        return nodesGeometry;
    }
}