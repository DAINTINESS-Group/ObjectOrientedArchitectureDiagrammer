package model.diagram;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.tree.Node;
import model.tree.Relationship;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DiagramArrangement {
    protected final Map<Integer, List<Double>> nodesGeometry;

    public DiagramArrangement() {
        nodesGeometry = new HashMap<>();
    }

    /**
     * This method is responsible for the arrangement of the graph by creating a graph of the Jung library
     * and using the SpringLayout algorithm
     * @param graphNodes the nodes of the diagram
     * @param graphEdges the edges of the diagram
     * @return a Map with the nodes' id as key and geometry(x,y) as value
     */
    public Map<Integer, List<Double>> arrangeDiagram(Map<Node, Integer> graphNodes, Map<Relationship, Integer> graphEdges) {
        Graph<Integer, String> graph = new SparseGraph<>();
        populateGraph(graphNodes, graphEdges, graph);
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        populateNodesGeometry(layout, new ArrayList<>(graphNodes.values()));
        return nodesGeometry;
    }

    private void populateGraph(Map<Node, Integer> graphNodes, Map<Relationship, Integer> graphEdges, Graph<Integer, String> graph){
        addVertexes(graphNodes, graph);
        addEdges(graphNodes, graphEdges, graph);
    }

    private void addVertexes(Map<Node, Integer> graphNodes, Graph<Integer, String> graph) {
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }
    }

    private void addEdges(Map<Node, Integer> graphNodes, Map<Relationship, Integer> graphEdges, Graph<Integer, String> graph) {
        for (Map.Entry<Relationship, Integer> entry : graphEdges.entrySet()) {
            graph.addEdge(graphNodes.get(entry.getKey().getStartingNode()) + " " + graphNodes.get(entry.getKey().getEndingNode()),
                    graphNodes.get(entry.getKey().getStartingNode()), graphNodes.get(entry.getKey().getEndingNode()), EdgeType.DIRECTED);
        }
    }
    private void populateNodesGeometry(AbstractLayout<Integer, String> layout, List<Integer> graphNodesIds) {
        for (Integer i : graphNodesIds) {
            nodesGeometry.put(i, Arrays.asList(layout.getX(i), layout.getY(i)));
        }
    }
}