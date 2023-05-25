package model.diagram.graphml;

import model.tree.node.Node;
import model.tree.node.PackageNode;

import java.util.Arrays;
import java.util.List;

public class GraphMLPackageNode {

    private static final int X_COORDINATE = 0;
    private static final int Y_COORDINATE = 1;

    public String convertNode(Node packageNode, int nodeId, List<Double> nodeGeometry) {
        return GraphMLSyntax.getInstance().getGraphMLPackageNodesSyntax(getNodesDescription((PackageNode)packageNode,
                nodeId, nodeGeometry));
    }

    private List<String> getNodesDescription(PackageNode packageNode, int nodeId, List<Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), packageNode.getName(), String.valueOf(nodeGeometry.get(X_COORDINATE)),
                String.valueOf(nodeGeometry.get(Y_COORDINATE)));
    }

	public String convertPlantNode(Node node) {
		// Do nothing
		return null;
	}
}
