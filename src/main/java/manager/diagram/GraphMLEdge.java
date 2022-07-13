package manager.diagram;

import model.LeafNode;
import model.PackageNode;
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
    private final Map<Integer, Integer> graphMLEdges;
    private final StringBuilder graphMLBuffer;
    private int edgeCounter;

    public GraphMLEdge() {
        graphMLEdges = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        edgeCounter = 0;
    }

    public void populateGraphMLEdges(PackageNode currentPackage, Map<LeafNode, Integer> graphMLNodes) {
        this.graphMLNodes = graphMLNodes;
        for (LeafNode l: currentPackage.getLeafNodes().values()) {
            generateEdge(l);
        }
    }

    private void generateEdge(LeafNode l) {
        for (Relationship branch: l.getLeafBranches()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLEdgesSyntax(getEdgesDescription(branch)));
            graphMLEdges.put(graphMLNodes.get(branch.getStartingLeafNode()), graphMLNodes.get(branch.getEndingLeafNode()));
            edgeCounter++;
        }
    }

    private List<String> getEdgesDescription(Relationship branch) {
        return Arrays.asList(String.valueOf(edgeCounter), String.valueOf(graphMLNodes.get(branch.getStartingLeafNode())),
                String.valueOf(graphMLNodes.get(branch.getEndingLeafNode())), identifyEdgeType(branch).get(EDGE_TYPE),
                identifyEdgeType(branch).get(EDGES_SOURCE_TYPE), identifyEdgeType(branch).get(EDGES_TARGET_TYPE));
    }

    private List<String> identifyEdgeType(Relationship branch){
        switch (branch.getRelationshipType()) {
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

    public Map<Integer, Integer> getGraphMLEdges() {
        return graphMLEdges;
    }
}
