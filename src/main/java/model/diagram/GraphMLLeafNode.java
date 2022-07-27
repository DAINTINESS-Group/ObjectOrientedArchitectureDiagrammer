package model.diagram;

import model.tree.LeafNode;
import model.tree.Node;
import model.tree.NodeType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLLeafNode extends GraphNode {

    private static final String CLASS_COLOR = "#FF9900";
    private static final String INTERFACE_COLOR = "#3366FF";

    public void convertNode(Node leafNode, int nodeId, List<Double> nodeGeometry) {
        graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLNodesSyntax(getNodesDescription((LeafNode) leafNode, nodeId, nodeGeometry)));
    }

    private List<String> getNodesDescription(LeafNode leafNode, int nodeId, List<Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), getNodesColor(leafNode), leafNode.getName(), getNodesFields(leafNode),
                getNodesMethods(leafNode), String.valueOf(nodeGeometry.get(X_COORDINATE)), String.valueOf(nodeGeometry.get(Y_COORDINATE)));
    }

    private String getNodesMethods(LeafNode leafNode) {
        if (leafNode.getMethods().size() == 0) {
            return "";
        }
        StringBuilder methods = new StringBuilder();
        for(Map.Entry<String, String> entry: leafNode.getMethods().entrySet()) {
            methods.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        return methods.deleteCharAt(methods.length() - 1).toString();
    }

    private String getNodesFields(LeafNode leafNode) {
        if (leafNode.getFields().size() == 0) {
            return "";
        }
        StringBuilder fields = new StringBuilder();
        for(Map.Entry<String, String> entry: leafNode.getFields().entrySet()) {
            fields.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        return fields.deleteCharAt(fields.length() - 1).toString();
    }

    private String getNodesColor(LeafNode leafNode) {
        if (leafNode.getType().equals(NodeType.INTERFACE)) {
            return INTERFACE_COLOR;
        }
        return CLASS_COLOR;
    }

}
