package manager.diagram;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.LeafNode;
import model.Relationship;

import java.util.Map;
import java.util.stream.Collectors;

public class ClassDiagramArrangement extends DiagramArrangement{

    public void populateGraph(Map<Object, Integer> graphNodes, Map<Object, Integer> graphEdges, Graph<Integer, String> graph) {
        addVertexes(graphNodes.entrySet().stream()
                .collect(Collectors.toMap(e -> (LeafNode)(e.getKey()), Map.Entry::getValue)), graph);
        addEdges(graphNodes.entrySet().stream().collect(Collectors.toMap(e -> (LeafNode)(e.getKey()), Map.Entry::getValue)),
                graphEdges.entrySet().stream().collect(Collectors.toMap(e -> (Relationship<?>)(e.getKey()), Map.Entry::getValue)), graph);
    }

    private void addVertexes(Map<LeafNode, Integer> graphNodes, Graph<Integer, String> graph) {
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }
    }

    private void addEdges(Map<LeafNode, Integer> graphNodes, Map<Relationship<?>, Integer> graphEdges, Graph<Integer, String> graph) {
        for (Map.Entry<Relationship<?>, Integer> entry : graphEdges.entrySet()) {
            graph.addEdge(graphNodes.get((LeafNode) entry.getKey().getStartingNode()) + " " + graphNodes.get((LeafNode)entry.getKey().getEndingNode()),
                    graphNodes.get((LeafNode)entry.getKey().getStartingNode()), graphNodes.get((LeafNode)entry.getKey().getEndingNode()), EdgeType.DIRECTED);
        }
    }

}
