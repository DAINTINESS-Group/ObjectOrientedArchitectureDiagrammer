package manager.diagram;

import model.LeafNode;
import model.PackageNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphMLPackageNode {

    private final Map<PackageNode, Integer> graphMLNodes;
    private final StringBuilder graphMLBuffer;
    private int nodeCounter;

    public GraphMLPackageNode() {
        graphMLNodes = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        nodeCounter = 0;
    }

    public void populateGraphMLNodes(PackageNode p) {
        graphMLNodes.put(p, nodeCounter);
        nodeCounter++;
    }

    public void convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry){
        for (Map.Entry<PackageNode, Integer> entry: graphMLNodes.entrySet()) {
            convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue()));
        }
    }

    private void convertNode(PackageNode p, int nodeId, List<Double> nodeGeometry) {
        graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPackageNodesSyntax(getNodesDescription(p, nodeId, nodeGeometry)));
    }

    private List<String> getNodesDescription(PackageNode p, int nodeId, List<Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), p.getName(), String.valueOf(nodeGeometry.get(0)), String.valueOf(nodeGeometry.get(1)));
    }

    public Map<PackageNode, Integer> getGraphMLNodes() {
        return graphMLNodes;
    }

    public String getGraphMLBuffer(){ return graphMLBuffer.toString(); }
}
