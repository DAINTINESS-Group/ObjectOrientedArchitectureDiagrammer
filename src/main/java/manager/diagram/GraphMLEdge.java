package manager.diagram;

import model.LeafNode;
import model.PackageNode;
import model.RelationshipBranch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMLEdge {

    private Map<LeafNode, Integer> graphMLNodes;
    private final Map<Integer, Integer> graphEdges;
    private final StringBuffer graphMLBuffer;
    private int edgeCounter;

    public GraphMLEdge() {
        graphEdges = new HashMap<>();
        graphMLBuffer = new StringBuffer();
        edgeCounter = 0;
    }

    public void populateGraphMLEdges(PackageNode currentPackage, Map<LeafNode, Integer> graphMLNodes) {
        this.graphMLNodes = graphMLNodes;
        for (LeafNode l: currentPackage.getLeafNodes().values()) {
            generateEdge(l, currentPackage);
        }
    }

    private void generateEdge(LeafNode l, PackageNode currentPackage) {
        for (RelationshipBranch branch: l.getLeafBranches()) {
            //Remove the if statement when working with all packages
            if (isEndingLeafNodeInDifferentPackage(branch, currentPackage)) {
                continue;
            }
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLEdgesSyntax(getEdgesDescription(branch)));
            graphEdges.put(graphMLNodes.get(branch.getStartingLeafNode()), graphMLNodes.get(branch.getEndingLeafNode()));
            edgeCounter++;
        }
    }

    private boolean isEndingLeafNodeInDifferentPackage(RelationshipBranch branch, PackageNode currentPackage) {
        return !currentPackage.getLeafNodes().containsKey(branch.getEndingLeafNode().getName());
    }

    private List<String> getEdgesDescription(RelationshipBranch branch) {
        return Arrays.asList(String.valueOf(edgeCounter), String.valueOf(graphMLNodes.get(branch.getStartingLeafNode())),
                String.valueOf(graphMLNodes.get(branch.getEndingLeafNode())), identifyEdgeType(branch).get(0),
                identifyEdgeType(branch).get(1), identifyEdgeType(branch).get(2));
    }

    private List<String> identifyEdgeType(RelationshipBranch branch){
        switch (branch.getBranchType()) {
            case "dependency":
                return Arrays.asList("dashed", "none", "plain");
            case "aggregation":
                return Arrays.asList("line", "white_diamond", "none");
            case "association":
                return Arrays.asList("line", "none", "standard");
            case "extension":
                return Arrays.asList("line", "none", "white_delta");
            default:
                return Arrays.asList("dashed", "none", "white_delta");
        }
    }

    public String getGraphMLBuffer() { return graphMLBuffer.toString();}

    public Map<Integer, Integer> getGraphEdges() {
        return graphEdges;
    }
}
