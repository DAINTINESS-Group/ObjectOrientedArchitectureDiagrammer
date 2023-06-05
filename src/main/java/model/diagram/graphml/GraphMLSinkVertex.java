package model.diagram.graphml;

import model.graph.SinkVertex;
import model.graph.VertexType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLSinkVertex {

    private static final String CLASS_COLOR = "#FF9900";
    private static final String INTERFACE_COLOR = "#3366FF";
    private static final int X_COORDINATE = 0;
    private static final int Y_COORDINATE = 1;
    private final Map<SinkVertex, Integer> graphNodes;
    private final StringBuilder graphMLBuffer;
    private final Map<Integer, List<Double>> nodesGeometry;

    public GraphMLSinkVertex(Map<SinkVertex, Integer> graphNodes, Map<Integer, List<Double>> nodesGeometry) {
        this.graphNodes = graphNodes;
        this.graphMLBuffer = new StringBuilder();
        this.nodesGeometry = nodesGeometry;
    }

    public StringBuilder convertLeafNode() {
        for (Map.Entry<SinkVertex, Integer> sinkVertex: graphNodes.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLNodesSyntax(
                    getNodesDescription(sinkVertex.getKey(), sinkVertex.getValue(), nodesGeometry.get(sinkVertex.getValue()))));
        }
        return graphMLBuffer;
    }

    private List<String> getNodesDescription(SinkVertex leafNode, int nodeId, List<Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), getNodesColor(leafNode), leafNode.getName(), getNodesFields(leafNode),
                getNodesMethods(leafNode), String.valueOf(nodeGeometry.get(X_COORDINATE)), String.valueOf(nodeGeometry.get(Y_COORDINATE)));
    }

    private String getNodesMethods(SinkVertex sinkVertex) {
        if (sinkVertex.getMethods().size() == 0) {
            return "";
        }
        StringBuilder methods = new StringBuilder();
        for (SinkVertex.Method method: sinkVertex.getMethods()) {
            methods.append(method.getReturnType()).append(" ").append(method.getName()).append("\n");
        }
        return methods.deleteCharAt(methods.length() - 1).toString();
    }

    private String getNodesFields(SinkVertex sinkVertex) {
        if (sinkVertex.getFields().size() == 0) {
            return "";
        }
        StringBuilder fields = new StringBuilder();
        for (SinkVertex.Field field: sinkVertex.getFields()) {
            fields.append(field.getType()).append(" ").append(field.getName()).append("\n");
        }
        return fields.deleteCharAt(fields.length() - 1).toString();
    }

    private String getNodesColor(SinkVertex leafNode) {
        if (leafNode.getVertexType().equals(VertexType.INTERFACE)) {
            return INTERFACE_COLOR;
        }
        return CLASS_COLOR;
    }

}
