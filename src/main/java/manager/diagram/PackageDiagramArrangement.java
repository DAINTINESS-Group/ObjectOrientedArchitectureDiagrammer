package manager.diagram;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.Relationship;
import model.PackageNode;

import java.util.Map;
import java.util.stream.Collectors;

public class PackageDiagramArrangement extends DiagramArrangement{


    @Override
    public void populateGraph(Map<Object, Integer> graphNodes, Map<Object, Integer> graphEdges, Graph<Integer, String> graph) {
        addVertexes(graphNodes.entrySet().stream()
                .collect(Collectors.toMap(e -> (PackageNode)(e.getKey()), Map.Entry::getValue)), graph);
        addEdges(graphNodes.entrySet().stream()
                .collect(Collectors.toMap(e -> (PackageNode)(e.getKey()), Map.Entry::getValue)),
                graphEdges.entrySet().stream().collect(Collectors.toMap(e -> (Relationship<?>)(e.getKey()), Map.Entry::getValue)), graph);
    }

    private void addVertexes(Map<PackageNode, Integer> graphNodes, Graph<Integer, String> graph) {
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }
    }

    private void addEdges(Map<PackageNode, Integer> graphNodes, Map<Relationship<?>, Integer> graphEdges, Graph<Integer, String> graph) {
        for (Map.Entry<Relationship<?>, Integer> entry : graphEdges.entrySet()) {
            graph.addEdge(graphNodes.get((PackageNode)entry.getKey().getStartingNode()) + " " + graphNodes.get((PackageNode) entry.getKey().getEndingNode()),
                    graphNodes.get((PackageNode)entry.getKey().getStartingNode()), graphNodes.get((PackageNode)entry.getKey().getEndingNode()), EdgeType.DIRECTED);
        }
    }
}
