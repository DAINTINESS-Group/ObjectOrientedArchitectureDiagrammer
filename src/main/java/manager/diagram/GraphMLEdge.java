package manager.diagram;

import model.LeafNode;
import model.Relationship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMLEdge {

    private static final int EDGE_TYPE = 0;
    private static final int EDGES_SOURCE_TYPE = 1;
    private static final int EDGES_TARGET_TYPE = 2;

    private Map<LeafNode, Integer> graphMLNodes;
    private final Map<Relationship<?>, Integer> graphMLEdges;
    private final StringBuilder graphMLBuffer;
    private int edgeCounter;

    public GraphMLEdge() {
        graphMLEdges = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        edgeCounter = 0;
    }

    public void populateGraphMLEdges(List<LeafNode> leafNodes, Map<LeafNode, Integer> graphMLNodes) {
        this.graphMLNodes = graphMLNodes;
        for (LeafNode l: leafNodes) {
            generateEdge(l);
        }
    }

    private void generateEdge(LeafNode l) {
        for (Relationship<?> relationship: l.getLeafNodeRelationships()) {
            if (areEdgesNodesInTheChosenClasses(relationship)) {
                graphMLEdges.put(relationship, edgeCounter);
                edgeCounter++;
            }
        }
    }

    private boolean areEdgesNodesInTheChosenClasses(Relationship<?> relationship) {
        return graphMLNodes.containsKey((LeafNode) relationship.getEndingNode());
    }

    public void convertEdgesToGraphML() {
        for (Map.Entry<Relationship<?>, Integer> entry: graphMLEdges.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLEdgesSyntax(getEdgesProperties(entry.getKey(), entry.getValue())));
        }
    }

    private List<String> getEdgesProperties(Relationship<?> branch, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphMLNodes.get((LeafNode) branch.getStartingNode())),
                String.valueOf(graphMLNodes.get((LeafNode) branch.getEndingNode())), identifyEdgeType(branch).get(EDGE_TYPE),
                identifyEdgeType(branch).get(EDGES_SOURCE_TYPE), identifyEdgeType(branch).get(EDGES_TARGET_TYPE));
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

    public String getGraphMLBuffer() { return graphMLBuffer.toString(); }

    public Map<Relationship<?>, Integer> getGraphMLEdges() {
        return graphMLEdges;
    }
}
