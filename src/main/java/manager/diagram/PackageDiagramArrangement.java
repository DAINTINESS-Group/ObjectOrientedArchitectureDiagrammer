package manager.diagram;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.LeafNode;
import model.LeafNodeRelationship;
import model.PackageNode;
import model.PackageNodeRelationship;

import java.util.Map;
import java.util.stream.Collectors;

public class PackageDiagramArrangement extends DiagramArrangement{


    @Override
    public void populateGraph(Map<Object, Integer> graphNodes, Map<Object, Integer> graphEdges, Graph<Integer, String> graph) {
        Map<PackageNode, Integer> newMap = graphNodes.entrySet().stream()
                .collect(Collectors.toMap(e -> (PackageNode)(e.getKey()), Map.Entry::getValue));
        Map<PackageNodeRelationship, Integer> newMap2 = graphEdges.entrySet().stream()
                .collect(Collectors.toMap(e -> (PackageNodeRelationship)(e.getKey()), Map.Entry::getValue));

        addVertexes(newMap, graph);
        addEdges(newMap, newMap2, graph);
    }

    private void addVertexes(Map<PackageNode, Integer> graphNodes, Graph<Integer, String> graph) {
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }
    }

    private void addEdges(Map<PackageNode, Integer> graphNodes, Map<PackageNodeRelationship, Integer> graphEdges, Graph<Integer, String> graph) {
        for (Map.Entry<PackageNodeRelationship, Integer> entry : graphEdges.entrySet()) {
            graph.addEdge(graphNodes.get(entry.getKey().getStartingPackageNode()) + " " + graphNodes.get(entry.getKey().getEndingPackageNode()),
                    graphNodes.get(entry.getKey().getStartingPackageNode()), graphNodes.get(entry.getKey().getEndingPackageNode()), EdgeType.DIRECTED);
        }
    }
}
