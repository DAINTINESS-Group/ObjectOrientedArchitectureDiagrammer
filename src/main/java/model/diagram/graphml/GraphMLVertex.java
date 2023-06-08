package model.diagram.graphml;

import model.graph.Vertex;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLVertex {

    private static final int X_COORDINATE = 0;
    private static final int Y_COORDINATE = 1;
    private final Map<Vertex, Integer> graphNodes;
    private final StringBuilder graphMLBuffer;
    private final Map<Integer, List<Double>> nodesGeometry;

    public GraphMLVertex(Map<Vertex, Integer> graphNodes, Map<Integer, List<Double>> nodesGeometry) {
        this.graphNodes = graphNodes;
        this.graphMLBuffer = new StringBuilder();
        this.nodesGeometry = nodesGeometry;
    }

    public StringBuilder convertVertex() {
        for (Map.Entry<Vertex, Integer> entry: graphNodes.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLVertexSyntax(getVertexDescription(entry.getKey(),
                    entry.getValue(), nodesGeometry.get(entry.getValue()))));
        }
        return graphMLBuffer;
    }

    private List<String> getVertexDescription(Vertex packageNode, int nodeId, List<Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), packageNode.getName(), String.valueOf(nodeGeometry.get(X_COORDINATE)),
                String.valueOf(nodeGeometry.get(Y_COORDINATE)));
    }

}
