package model.diagram;

import model.tree.Relationship;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLPackageEdge extends GraphEdge {

    public void convertEdgesToGraphML() {
        for (Map.Entry<Relationship, Integer> entry: graphEdges.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPackageEdgesSyntax(getEdgesProperties(entry.getKey(), entry.getValue())));
        }
    }

    private List<String> getEdgesProperties(Relationship relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphNodes.get(relationship.getStartingNode())),
                String.valueOf(graphNodes.get(relationship.getEndingNode())));
    }

}
