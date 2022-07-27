package model.diagram;

import model.tree.Node;
import model.tree.PackageNode;

import java.util.Arrays;
import java.util.List;

public class GraphMLPackageNode extends GraphNode {

    public void convertNode(Node packageNode, int nodeId, List<Double> nodeGeometry) {
        graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPackageNodesSyntax(getNodesDescription((PackageNode)packageNode,
                nodeId, nodeGeometry)));
    }

    private List<String> getNodesDescription(PackageNode packageNode, int nodeId, List<Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), packageNode.getName(), String.valueOf(nodeGeometry.get(X_COORDINATE)),
                String.valueOf(nodeGeometry.get(Y_COORDINATE)));
    }
}
