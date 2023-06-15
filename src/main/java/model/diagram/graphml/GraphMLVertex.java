package model.diagram.graphml;

import model.graph.Vertex;
import org.javatuples.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLVertex {

    private final Map<Vertex, Integer> graphNodes;
    private final StringBuilder graphMLBuffer;
    private final Map<Integer, Pair<Double, Double>> nodesGeometry;

    public GraphMLVertex(Map<Vertex, Integer> graphNodes, Map<Integer, Pair<Double, Double>> nodesGeometry) {
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

    private List<String> getVertexDescription(Vertex packageNode, int nodeId, Pair<Double, Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), packageNode.getName(), String.valueOf(nodeGeometry.getValue0()),
                String.valueOf(nodeGeometry.getValue1()));
    }

}
