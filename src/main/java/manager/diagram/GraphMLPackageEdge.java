package manager.diagram;

import model.Relationship;
import model.PackageNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMLPackageEdge {

    private Map<PackageNode, Integer> graphMLNodes;
    private final Map<Relationship<?>, Integer> graphMLEdges;
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
        for (Relationship<?> relationship: p.getPackageNodeRelationships()) {
            if (areEdgesNodesInTheChosenClasses(relationship)) {
                graphMLEdges.put(relationship, edgeCounter);
                edgeCounter++;
            }
        }
    }

    private boolean areEdgesNodesInTheChosenClasses(Relationship<?> relationship) {
        return graphMLNodes.containsKey((PackageNode) relationship.getEndingNode());
    }

    public void convertEdgesToGraphML() {
        for (Map.Entry<Relationship<?>, Integer> entry: graphMLEdges.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPackageEdgesSyntax(getEdgesProperties(entry.getKey(), entry.getValue())));
        }
    }

    private List<String> getEdgesProperties(Relationship<?> relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphMLNodes.get((PackageNode)relationship.getStartingNode())),
                String.valueOf(graphMLNodes.get((PackageNode)relationship.getEndingNode())));
    }

    public Map<Relationship<?>, Integer> getGraphMLEdges() {
        return graphMLEdges;
    }

    public String getGraphMLBuffer() { return graphMLBuffer.toString(); }
}
