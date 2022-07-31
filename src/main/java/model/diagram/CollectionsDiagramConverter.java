package model.diagram;

import model.tree.Node;
import model.tree.Relationship;

import java.util.HashMap;
import java.util.Map;

public class CollectionsDiagramConverter {

    private final GraphNodeCollection graphNodeCollection;
    private final GraphEdgeCollection graphEdgeCollection;

    public CollectionsDiagramConverter(GraphNodeCollection graphNodeCollection, GraphEdgeCollection graphEdgeCollection) {
        this.graphNodeCollection = graphNodeCollection;
        this.graphEdgeCollection = graphEdgeCollection;
    }

    public Map<String, Map<String, String>> convertCollectionsToDiagram() {
        Map<String, Map<String, String>> graph = new HashMap<>();
        iterateGraphNodes(graph);
        return graph;
    }

    private void iterateGraphNodes(Map<String, Map<String, String>> graph) {
        for (Node leafNode: graphNodeCollection.getGraphNodes().keySet()) {
            Map<String, String> nodeEdges = new HashMap<>();
            iterateGraphEdges(leafNode, nodeEdges);
            insertNode(graph, leafNode, nodeEdges);
        }
    }

    private void iterateGraphEdges(Node leafNode, Map<String, String> nodeEdges) {
        for (Relationship relationship: graphEdgeCollection.getGraphEdges().keySet()){
            if (doesEdgeStartFromCurrentNode(leafNode, relationship)) {
                insertEdge(nodeEdges, relationship);
            }
        }
    }

    private void insertNode(Map<String, Map<String, String>> graph, Node leafNode, Map<String, String> nodeEdges) {
        graph.put(leafNode.getName() + "_" + leafNode.getType().name(), nodeEdges);
    }

    private boolean doesEdgeStartFromCurrentNode(Node leafNode, Relationship relationship) {
        return relationship.getStartingNode().equals(leafNode);
    }

    private void insertEdge(Map<String, String> nodeEdges, Relationship relationship) {
        nodeEdges.put(relationship.getEndingNode().getName() + "_" + relationship.getEndingNode().getType().name(),
                relationship.getRelationshipType().name());
    }
}
