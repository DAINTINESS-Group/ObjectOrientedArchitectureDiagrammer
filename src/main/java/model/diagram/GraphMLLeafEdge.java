package model.diagram;

import model.tree.LeafNode;
import model.tree.Relationship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMLLeafEdge {

    private static final int EDGE_TYPE = 0;
    private static final int EDGES_SOURCE_TYPE = 1;
    private static final int EDGES_TARGET_TYPE = 2;

    private final Map<Relationship<LeafNode>, Integer> graphMLEdges;
    private Map<LeafNode, Integer> graphMLNodes;
    private final StringBuilder graphMLBuffer;
    private int edgeId;

    public GraphMLLeafEdge() {
        graphMLEdges = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        edgeId = 0;
    }

    public void populateGraphMLEdges(List<LeafNode> leafNodes) {
        for (LeafNode leafNode: leafNodes) {
            generateEdge(leafNode);
        }
    }


    public void generateEdge(LeafNode leafNode) {
        for (Relationship<LeafNode> relationship: leafNode.getLeafNodeRelationships()) {
            if (areEdgesNodesInTheChosenClasses(relationship)) {
                addEdge(relationship);
            }
        }
    }

    private boolean areEdgesNodesInTheChosenClasses(Relationship<LeafNode> relationship) {
        return graphMLNodes.containsKey(relationship.getEndingNode());
    }

    private void addEdge(Relationship<LeafNode> relationship) {
        graphMLEdges.put(relationship, edgeId);
        edgeId++;
    }

    public void convertEdgesToGraphML() {
        for (Map.Entry<Relationship<LeafNode>, Integer> entry: graphMLEdges.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLEdgesSyntax(getEdgesProperties(entry.getKey(), entry.getValue())));
        }
    }

    private List<String> getEdgesProperties(Relationship<LeafNode> relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphMLNodes.get(relationship.getStartingNode())),
                String.valueOf(graphMLNodes.get(relationship.getEndingNode())), identifyEdgeType(relationship).get(EDGE_TYPE),
                identifyEdgeType(relationship).get(EDGES_SOURCE_TYPE), identifyEdgeType(relationship).get(EDGES_TARGET_TYPE));
    }

    private List<String> identifyEdgeType(Relationship<?> relationship){
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

    public Map<Relationship<LeafNode>, Integer> getGraphMLEdges() {
        return graphMLEdges;
    }

    public String getGraphMLBuffer() { return graphMLBuffer.toString(); }

    public void setGraphMLNodes(Map<LeafNode, Integer> graphMLNodes) {
        this.graphMLNodes = graphMLNodes;
    }

}
