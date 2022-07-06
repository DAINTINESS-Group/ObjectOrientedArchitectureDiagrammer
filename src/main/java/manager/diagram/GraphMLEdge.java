package manager.diagram;

import model.LeafNode;
import model.PackageNode;
import model.RelationshipBranch;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLEdge {

    private String graphMLFile;
    private Map<LeafNode, Integer> graphMLNodes;

    public GraphMLEdge(PackageNode currentPackage, String graphMLFile, Map<LeafNode, Integer> graphMLNodes) {
        this.graphMLNodes = graphMLNodes;
        this.graphMLFile = graphMLFile;
        parseLeafNodes(currentPackage);
    }

    private void parseLeafNodes(PackageNode currentPackage) {
        int edgeCounter = 0;
        for (LeafNode l: currentPackage.getLeafNodes().values()) {
            edgeCounter = generateEdge(l, edgeCounter, currentPackage);
        }
    }

    private int generateEdge(LeafNode l, int edgeId, PackageNode currentPackage) {
        //types
        //implementation -> type dashed arrowtarget white_delta
        //extension -> type line arrowtarget white_delta
        //dependency -> type dashed arrowtarget plain
        //aggregation -> type line arrowtarget white_diamond (reverse source & target)
        //association -> type line arrowtarget standard
        for (RelationshipBranch branch: l.getLeafBranches()) {
            if (isEndingLeafNodeInDifferentPackage(branch, currentPackage)) {
                continue;
            }
            graphMLFile += GraphMLSyntax.getInstance().getGraphMLEdgesSyntax(Arrays.asList(String.valueOf(edgeId), String.valueOf(graphMLNodes.get(branch.getStartingLeafNode())),
                    String.valueOf(graphMLNodes.get(branch.getEndingLeafNode())), identifyEdgeType(branch).get(0), identifyEdgeType(branch).get(1),
                    identifyEdgeType(branch).get(2)));
            edgeId++;
        }
        return edgeId;
    }

    private boolean isEndingLeafNodeInDifferentPackage(RelationshipBranch branch, PackageNode currentPackage) {
        return !currentPackage.getLeafNodes().containsKey(branch.getEndingLeafNode().getName());
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

    public String getGraphMLFile() {
        return graphMLFile;
    }

}
