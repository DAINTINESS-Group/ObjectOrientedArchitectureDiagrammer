package model.diagram.graphml;

import model.tree.edge.Relationship;
import model.tree.node.Node;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLLeafEdge {

    private static final int EDGE_TYPE = 0;
    private static final int EDGES_SOURCE_TYPE = 1;
    private static final int EDGES_TARGET_TYPE = 2;
    private final Map<Node, Integer> graphNodes;

    public GraphMLLeafEdge(Map<Node, Integer> graphNodes) {
        this.graphNodes = graphNodes;
    }

    public String convertEdge(Relationship relationship, int edgeId) {
        return GraphMLSyntax.getInstance().getGraphMLEdgesSyntax(getEdgesProperties(relationship, edgeId));
    }

    private List<String> getEdgesProperties(Relationship relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphNodes.get(relationship.getStartingNode())),
                String.valueOf(graphNodes.get(relationship.getEndingNode())), identifyEdgeType(relationship).get(EDGE_TYPE),
                identifyEdgeType(relationship).get(EDGES_SOURCE_TYPE), identifyEdgeType(relationship).get(EDGES_TARGET_TYPE));
    }

    private List<String> identifyEdgeType(Relationship relationship){
        return switch (relationship.getRelationshipType()) {
            case DEPENDENCY -> Arrays.asList("dashed", "none", "plain");
            case AGGREGATION -> Arrays.asList("line", "white_diamond", "none");
            case ASSOCIATION -> Arrays.asList("line", "none", "standard");
            case EXTENSION -> Arrays.asList("line", "none", "white_delta");
            default -> Arrays.asList("dashed", "none", "white_delta");
        };
    }

}
