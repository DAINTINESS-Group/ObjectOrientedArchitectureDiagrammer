package model.diagram;

import model.tree.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GraphNode {

    protected static final int X_COORDINATE = 0;
    protected static final int Y_COORDINATE = 1;

    private final Map<Node, Integer> graphNodes;
    protected final StringBuilder graphMLBuffer;
    private int nodeId;

    public GraphNode() {
        graphNodes = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        nodeId = 0;
    }

    public void populateGraphMLNodes(List<Node> nodes){
        for (Node node: nodes) {
            graphNodes.put(node, nodeId);
            nodeId++;
        }
    }

    public void convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry) {
        for (Map.Entry<Node, Integer> entry: graphNodes.entrySet()) {
            convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue()));
        }
    }

    public Map<Node, Integer> getGraphNodes() {
        return graphNodes;
    }

    public String getGraphMLBuffer(){ return graphMLBuffer.toString(); }

    public abstract void convertNode(Node node, int nodeId, List<Double> nodesGeometry);

}
