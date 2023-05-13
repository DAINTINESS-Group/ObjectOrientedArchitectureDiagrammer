package model.diagram;

import model.tree.node.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GraphNodeCollection {

    protected static final int X_COORDINATE = 0;
    protected static final int Y_COORDINATE = 1;

    private final Map<Node, Integer> graphNodes;
    private final StringBuilder graphMLBuffer;
    private int nodeId;

    public GraphNodeCollection() {
        graphNodes = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        nodeId = 0;
    }

    public void populateGraphNodes(List<Node> nodes){
        for (Node node: nodes) {
            graphNodes.put(node, nodeId);
            nodeId++;
        }
    }

    public StringBuilder convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry) {
        for (Map.Entry<Node, Integer> entry: graphNodes.entrySet()) {
            graphMLBuffer.append(convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue())));
        }
        return graphMLBuffer;
    }

    public Map<Node, Integer> getGraphNodes() {
        return graphNodes;
    }

    public String getGraphMLBuffer(){ return graphMLBuffer.toString(); }

    public abstract String convertNode(Node node, int nodeId, List<Double> nodesGeometry);

}
