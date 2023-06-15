package model.diagram.graphml;

import model.graph.Arc;
import model.graph.SinkVertex;

import java.util.*;

public class GraphMLSinkVertexArc {

    private static final int EDGE_TYPE = 0;
    private static final int EDGES_SOURCE_TYPE = 1;
    private static final int EDGES_TARGET_TYPE = 2;
    private final Map<SinkVertex, Integer> graphNodes;
    private final Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;
    private final StringBuilder graphMLBuffer;

    public GraphMLSinkVertexArc(Map<SinkVertex, Integer> graphNodes, Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        this.graphNodes = graphNodes;
        this.diagram = diagram;
        graphMLBuffer = new StringBuilder();
    }

    public StringBuilder convertSinkVertexArc(Map<Arc<SinkVertex>, Integer> graphEdges) {
        for (Map.Entry<Arc<SinkVertex>, Integer> arc: graphEdges.entrySet()) {
            Optional<Arc<SinkVertex>> optionalArc = diagram.get(arc.getKey().getSourceVertex()).stream()
                .filter(sinkVertexArc ->
                    sinkVertexArc.getTargetVertex().equals(arc.getKey().getTargetVertex()) &&
                    sinkVertexArc.getArcType().equals(arc.getKey().getArcType()))
                .findFirst();
            if (optionalArc.isEmpty()) {
                continue;
            }

            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLSinkVertexArcSyntax(getEdgesProperties(arc.getKey(), arc.getValue())));
        }
        return graphMLBuffer;
    }

    private List<String> getEdgesProperties(Arc<SinkVertex> relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphNodes.get(relationship.getSourceVertex())),
                String.valueOf(graphNodes.get(relationship.getTargetVertex())), identifyEdgeType(relationship).get(EDGE_TYPE),
                identifyEdgeType(relationship).get(EDGES_SOURCE_TYPE), identifyEdgeType(relationship).get(EDGES_TARGET_TYPE));
    }

    private List<String> identifyEdgeType(Arc<SinkVertex> relationship){
        return switch (relationship.getArcType()) {
            case DEPENDENCY -> Arrays.asList("dashed", "none", "plain");
            case AGGREGATION -> Arrays.asList("line", "white_diamond", "none");
            case ASSOCIATION -> Arrays.asList("line", "none", "standard");
            case EXTENSION -> Arrays.asList("line", "none", "white_delta");
            default -> Arrays.asList("dashed", "none", "white_delta");
        };
    }

}
