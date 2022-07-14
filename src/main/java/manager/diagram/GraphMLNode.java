package manager.diagram;

import model.LeafNode;
import model.LeafNodeType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMLNode {

    private static final String CLASS_COLOR = "#FF9900";
    private static final String INTERFACE_COLOR = "#3366FF";

    private final Map<LeafNode, Integer> graphMLNodes;
    private final StringBuilder graphMLBuffer;
    private int nodeCounter;

    public GraphMLNode() {
        graphMLNodes = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        nodeCounter = 0;
    }

    public void populateGraphMLNodes(List<LeafNode> leafNodes) {
        for(LeafNode l: leafNodes) {
            graphMLNodes.put(l, nodeCounter);
            nodeCounter++;
        }
    }

    public void convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry){
        for (Map.Entry<LeafNode, Integer> entry: graphMLNodes.entrySet()) {
            convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue()));
        }
    }

    private void convertNode(LeafNode l, int nodeId, List<Double> nodeGeometry) {
        graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLNodesSyntax(getNodesDescription(l, nodeId, nodeGeometry)));
    }

    private List<String> getNodesDescription(LeafNode l, int nodeId, List<Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), getNodesColor(l), l.getName(), getNodesFields(l), getNodesMethods(l),
                String.valueOf(nodeGeometry.get(0)), String.valueOf(nodeGeometry.get(1)));
    }

    private String getNodesMethods(LeafNode l) {
        if (l.getMethods().size() == 0) {
            return "";
        }
        StringBuilder methods = new StringBuilder();
        for(Map.Entry<String, String> entry: l.getMethods().entrySet()) {
            methods.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        return methods.deleteCharAt(methods.length() - 1).toString();
    }

    private String getNodesFields(LeafNode l) {
        if (l.getFields().size() == 0) {
            return "";
        }
        StringBuilder fields = new StringBuilder();
        for(Map.Entry<String, String> entry: l.getFields().entrySet()) {
            fields.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        return fields.deleteCharAt(fields.length() - 1).toString();
    }

    private String getNodesColor(LeafNode l) {
        if (l.getType().equals(LeafNodeType.INTERFACE)) {
            return INTERFACE_COLOR;
        }
        return CLASS_COLOR;
    }

    public String getGraphMLBuffer(){ return graphMLBuffer.toString(); }

    public Map<LeafNode, Integer> getGraphMLNodes() {
        return graphMLNodes;
    }

}
