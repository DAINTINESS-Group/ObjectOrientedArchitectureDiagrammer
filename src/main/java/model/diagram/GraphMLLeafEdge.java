package model.diagram;

import model.tree.Relationship;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLLeafEdge extends GraphEdge {

    private static final int EDGE_TYPE = 0;
    private static final int EDGES_SOURCE_TYPE = 1;
    private static final int EDGES_TARGET_TYPE = 2;

    public void convertEdgesToGraphML() {
        for (Map.Entry<Relationship, Integer> entry: graphEdges.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLEdgesSyntax(getEdgesProperties(entry.getKey(), entry.getValue())));
        }
    }

    private List<String> getEdgesProperties(Relationship relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphNodes.get(relationship.getStartingNode())),
                String.valueOf(graphNodes.get(relationship.getEndingNode())), identifyEdgeType(relationship).get(EDGE_TYPE),
                identifyEdgeType(relationship).get(EDGES_SOURCE_TYPE), identifyEdgeType(relationship).get(EDGES_TARGET_TYPE));
    }

    private List<String> identifyEdgeType(Relationship relationship){
        switch (relationship.getRelationshipType()) {
            case DEPENDENCY:
                return Arrays.asList("dashed", "none", "plain");
            case AGGREGATION:
                return Arrays.asList("line", "white_diamond", "none");
            case ASSOCIATION:
                return Arrays.asList("line", "none", "standard");
            case EXTENSION:
                return Arrays.asList("line", "none", "white_delta");
            default:
                return Arrays.asList("dashed", "none", "white_delta");
        }
    }

}
