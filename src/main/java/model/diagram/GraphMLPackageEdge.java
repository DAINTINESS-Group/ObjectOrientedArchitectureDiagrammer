package model.diagram;

import model.tree.Relationship;
import model.tree.PackageNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMLPackageEdge {

    private final Map<Relationship<PackageNode>, Integer> graphMLEdges;
    private Map<PackageNode, Integer> graphMLNodes;
    private final StringBuilder graphMLBuffer;
    private int edgeId;

    public GraphMLPackageEdge() {
        graphMLEdges = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        edgeId = 0;
    }

    public void populateGraphMLEdges(List<PackageNode> packageNodes) {
        for (PackageNode packageNode: packageNodes) {
            generateEdge(packageNode);
        }
    }

    public void generateEdge(PackageNode packageNode) {
        for (Relationship<PackageNode> relationship: packageNode.getPackageNodeRelationships()) {
            if (areEdgesNodesInTheChosenPackages(relationship)) {
                addEdge(relationship);
            }
        }
    }

    private boolean areEdgesNodesInTheChosenPackages(Relationship<PackageNode> relationship) {
        return graphMLNodes.containsKey(relationship.getEndingNode());
    }

    private void addEdge(Relationship<PackageNode> relationship) {
        graphMLEdges.put(relationship, edgeId);
        edgeId++;
    }

    public void convertEdgesToGraphML() {
        for (Map.Entry<Relationship<PackageNode>, Integer> entry: graphMLEdges.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPackageEdgesSyntax(getEdgesProperties(entry.getKey(), entry.getValue())));
        }
    }

    private List<String> getEdgesProperties(Relationship<PackageNode> relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphMLNodes.get(relationship.getStartingNode())),
                String.valueOf(graphMLNodes.get(relationship.getEndingNode())));
    }

    public Map<Relationship<PackageNode>, Integer> getGraphMLEdges() {
        return graphMLEdges;
    }

    public void setGraphMLNodes(Map<PackageNode, Integer> graphMLNodes){
        this.graphMLNodes = graphMLNodes;
    }

    public String getGraphMLBuffer() { return graphMLBuffer.toString(); }

}
