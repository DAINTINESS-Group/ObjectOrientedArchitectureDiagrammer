package model.diagram;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;

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
    public Map<Integer, List<Double>> arrangeClassDiagram(Map<SinkVertex, Integer> graphNodes, Map<Arc<SinkVertex>, Integer> graphEdges) {
        Graph<Integer, String> graph = new SparseGraph<>();
        populateClassGraph(graphNodes, graphEdges, graph);
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        populateNodesGeometry(layout, new ArrayList<>(graphNodes.values()));
        return nodesGeometry;
    }

    public Map<Integer, List<Double>> arrangePackageDiagram(Map<Vertex, Integer> graphNodes, Map<Arc<Vertex>, Integer> graphEdges) {
        Graph<Integer, String> graph = new SparseGraph<>();
        //populateGraph(graphNodes, graphEdges, graph);
        populatePackageGraph(graphNodes, graphEdges, graph);
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        populateNodesGeometry(layout, new ArrayList<>(graphNodes.values()));
        return nodesGeometry;
    }

    private void populateClassGraph(Map<SinkVertex, Integer> graphNodes, Map<Arc<SinkVertex>, Integer> graphEdges, Graph<Integer, String> graph){
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }
        for (Map.Entry<Arc<SinkVertex>, Integer> arc : graphEdges.entrySet()) {
            graph.addEdge(graphNodes.get(arc.getKey().getSourceVertex()) + " " + graphNodes.get(arc.getKey().getTargetVertex()),
                    graphNodes.get(arc.getKey().getSourceVertex()), graphNodes.get(arc.getKey().getTargetVertex()), EdgeType.DIRECTED);
        }
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