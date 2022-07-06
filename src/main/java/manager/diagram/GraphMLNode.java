package manager.diagram;

import model.LeafNode;
import model.PackageNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GraphMLNode {

    private static final String CLASS_COLOR = "#FF9900";
    private static final String INTERFACE_COLOR = "#3366FF";

    private Map<LeafNode, Integer> graphMLNodes;
    private String graphMLFile;

    public GraphMLNode(PackageNode currentPackage, String graphMLFile) {
        this.graphMLFile = graphMLFile;
        parseLeafNodes(currentPackage);
    }

    private void parseLeafNodes(PackageNode currentPackage) {
        int nodeCounter = 0;
        graphMLNodes = new HashMap<>();
        for(LeafNode l: currentPackage.getLeafNodes().values()) {
            graphMLNodes.put(l, nodeCounter);
            generateNode(l, nodeCounter);
            nodeCounter++;
        }
    }

    private void generateNode(LeafNode l, int nodeId) {
        //TODO geometry -> method arrangeDiagram (force directed algorithm)
        graphMLFile += GraphMLSyntax.getInstance().getGraphMLNodesSyntax(Arrays.asList(String.valueOf(nodeId), getNodesColor(l), l.getName(),
                getNodesFields(l), getNodesMethods(l)));
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
        if (l.getType().equals("interface")) {
            return INTERFACE_COLOR;
        }
        return CLASS_COLOR;
    }

    public String getGraphMLFile() {
        return graphMLFile;
    }

    public Map<LeafNode, Integer> getGraphMLNodes() {
        return graphMLNodes;
    }

}
