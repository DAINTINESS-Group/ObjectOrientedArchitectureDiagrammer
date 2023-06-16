package model.diagram.graphml;

import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;

import java.util.*;

public class GraphMLVertexArc {
    private final Map<Vertex, Integer> graphNodes;
    private final Map<Vertex, Set<Arc<Vertex>>> diagram;
    private final StringBuilder graphMLBuffer;

    public GraphMLVertexArc(Map<Vertex, Integer> graphNodes, Map<Vertex, Set<Arc<Vertex>>> diagram) {
        this.graphNodes = graphNodes;
        this.diagram = diagram;
        graphMLBuffer = new StringBuilder();
    }

    public StringBuilder convertVertexArc() {
        List<Arc<Vertex>> arcs = new ArrayList<>();
        for (Set<Arc<Vertex>> arcSet: diagram.values()) {
            arcs.addAll(arcSet);
        }

        int edgeId = 0;
        for (Arc<Vertex> arc: arcs) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLVertexArcSyntax(getVertexArcProperties(arc, edgeId)));
            edgeId++;
        }
        return graphMLBuffer;
    }

    private List<String> getVertexArcProperties(Arc<Vertex> relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(graphNodes.get(relationship.getSourceVertex())),
                String.valueOf(graphNodes.get(relationship.getTargetVertex())));
    }

}
