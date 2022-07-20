package manager.diagram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GraphMLNode<T> {

    protected static final int X_COORDINATE = 0;
    protected static final int Y_COORDINATE = 1;

    private final Map<T, Integer> graphMLNodes;
    protected final StringBuilder graphMLBuffer;
    private int nodeId;

    public GraphMLNode() {
        graphMLNodes = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        nodeId = 0;
    }

    public void populateGraphMLNodes(List<T> nodes){
        for (T node: nodes) {
            graphMLNodes.put(node, nodeId);
            nodeId++;
        }
    }

    public void convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry) {
        for (Map.Entry<T, Integer> entry: graphMLNodes.entrySet()) {
            convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue()));
        }
    }

    public Map<T, Integer> getGraphMLNodes() {
        return graphMLNodes;
    }

    public String getGraphMLBuffer(){ return graphMLBuffer.toString(); }

    public abstract void convertNode(T node, int nodeId, List<Double> nodesGeometry);

}
