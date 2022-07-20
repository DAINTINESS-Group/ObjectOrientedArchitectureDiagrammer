package manager.diagram;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.Relationship;

import java.util.Map;

public class PackageDiagramArrangement<T> extends DiagramArrangement<T>{

    public void populateGraph(Map<T, Integer> graphNodes, Map<Relationship<T>, Integer> graphEdges, Graph<Integer, String> graph) {
        addVertexes(graphNodes, graph);
        addEdges(graphNodes, graphEdges, graph);
    }

    private void addVertexes(Map<T, Integer> graphNodes, Graph<Integer, String> graph) {
        for (Integer i : graphNodes.values()) {
            graph.addVertex(i);
        }
    }

    private void addEdges(Map<T, Integer> graphNodes, Map<Relationship<T>, Integer> graphEdges, Graph<Integer, String> graph) {
        for (Map.Entry<Relationship<T>, Integer> entry : graphEdges.entrySet()) {
            graph.addEdge(graphNodes.get(entry.getKey().getStartingNode()) + " " + graphNodes.get(entry.getKey().getEndingNode()),
                    graphNodes.get(entry.getKey().getStartingNode()), graphNodes.get(entry.getKey().getEndingNode()), EdgeType.DIRECTED);
        }
    }
}
