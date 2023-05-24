package model.diagram.graphml;

import model.diagram.GraphEdgeCollection;
import model.tree.edge.Relationship;

import java.util.Arrays;
import java.util.List;

public class GraphMLPackageEdge extends GraphEdgeCollection {

    public String convertEdge(Relationship relationship, int edgeId) {
        return GraphMLSyntax.getInstance().getGraphMLPackageEdgesSyntax(getEdgesProperties(relationship, edgeId));
    }

    private List<String> getEdgesProperties(Relationship relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphNodes.get(relationship.getStartingNode())),
                String.valueOf(graphNodes.get(relationship.getEndingNode())));
    }

	@Override
	public String convertPlantEdge(Relationship relationship) {
		// Do nothing
		return null;
	}

}
