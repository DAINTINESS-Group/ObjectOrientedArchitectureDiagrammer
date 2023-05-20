package model.diagram.graphml;

import model.tree.edge.Relationship;
import model.tree.node.Node;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLPackageEdge {
    private final Map<Node, Integer> graphNodes;

    public GraphMLPackageEdge(Map<Node, Integer> graphNodes) {

        this.graphNodes = graphNodes;
    }

    public String convertEdge(Relationship relationship, int edgeId) {
        return GraphMLSyntax.getInstance().getGraphMLPackageEdgesSyntax(getEdgesProperties(relationship, edgeId));
    }

    private List<String> getEdgesProperties(Relationship relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphNodes.get(relationship.getStartingNode())),
                String.valueOf(graphNodes.get(relationship.getEndingNode())));
    }

}
