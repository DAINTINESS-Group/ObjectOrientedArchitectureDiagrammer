package model.diagram.graphml;

import model.graph.Arc;
import model.graph.Vertex;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLPackageEdge {
    private final Map<Vertex, Integer> graphNodes;
    private final StringBuilder graphMLBuffer;

    public GraphMLPackageEdge(Map<Vertex, Integer> graphNodes) {
        this.graphNodes = graphNodes;
        graphMLBuffer = new StringBuilder();
    }

    public StringBuilder convertPackageEdge(Map<Arc<Vertex>, Integer> graphEdges) {
        for (Map.Entry<Arc<Vertex>, Integer> entry: graphEdges.entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPackageEdgesSyntax(getEdgesProperties(entry.getKey(), entry.getValue())));
        }
        return graphMLBuffer;
    }

    private List<String> getEdgesProperties(Arc<Vertex> relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphNodes.get(relationship.getSourceVertex())),
                String.valueOf(graphNodes.get(relationship.getTargetVertex())));
    }

}
