package manager.diagram;

import model.PackageNode;
import model.PackageNodeRelationship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMLPackageEdge {

    private Map<PackageNode, Integer> graphMLNodes;
    private final Map<PackageNodeRelationship, Integer> graphMLEdges;
    private final StringBuilder graphMLBuffer;
    private int edgeCounter;

    public GraphMLPackageEdge() {
        graphMLEdges = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        edgeCounter = 0;
    }

    public void populateGraphMLEdges(PackageNode p, Map<PackageNode, Integer> graphMLNodes) {
        this.graphMLNodes = graphMLNodes;
        generateEdge(p);
    }

    private void generateEdge(PackageNode p) {
        for (PackageNodeRelationship relationship: p.getPackageNodeRelationships()) {
            if (areEdgesNodesInTheChosenClasses(relationship)) {
                graphMLEdges.put(relationship, edgeCounter);
                edgeCounter++;
            }
        }
    }

    private boolean areEdgesNodesInTheChosenClasses(PackageNodeRelationship relationship) {
        return graphMLNodes.containsKey(relationship.getEndingPackageNode());
    }

    public void convertEdgesToGraphML() {
        for (Map.Entry<PackageNodeRelationship, Integer> entry: graphMLEdges.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPackageEdgesSyntax(getEdgesProperties(entry.getKey(), entry.getValue())));
        }
    }

    private List<String> getEdgesProperties(PackageNodeRelationship relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphMLNodes.get(relationship.getStartingPackageNode())),
                String.valueOf(graphMLNodes.get(relationship.getEndingPackageNode())));
    }

    public Map<PackageNodeRelationship, Integer> getGraphMLEdges() {
        return graphMLEdges;
    }

    public String getGraphMLBuffer() { return graphMLBuffer.toString(); }
}
