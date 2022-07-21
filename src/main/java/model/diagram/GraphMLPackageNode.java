package model.diagram;

import model.tree.PackageNode;

import java.util.Arrays;
import java.util.List;

public class GraphMLPackageNode<T> extends GraphMLNode<T> {

    public void convertNode(T packageNode, int nodeId, List<Double> nodeGeometry) {
        graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPackageNodesSyntax(getNodesDescription((PackageNode)packageNode,
                nodeId, nodeGeometry)));
    }

    private List<String> getNodesDescription(PackageNode packageNode, int nodeId, List<Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), packageNode.getName(), String.valueOf(nodeGeometry.get(X_COORDINATE)),
                String.valueOf(nodeGeometry.get(Y_COORDINATE)));
    }
}
