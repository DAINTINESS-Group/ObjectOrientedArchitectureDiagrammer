package model.diagram.plantuml;

import model.graph.Arc;
import model.graph.ArcType;
import model.graph.Vertex;

import java.util.Map;
import java.util.stream.Collectors;

public class PlantUMLVertexArc {

    private final Map<Arc<Vertex>, Integer> graphEdges;

    public PlantUMLVertexArc(Map<Arc<Vertex>, Integer> graphEdges) {
        this.graphEdges = graphEdges;
    }

    public StringBuilder convertVertexArc() {
        return new StringBuilder(
        graphEdges.keySet().stream()
            .map(vertexArc ->
                vertexArc.getSourceVertex().getName() + " " + getRelationship(vertexArc.getArcType()) + " " +
                vertexArc.getTargetVertex().getName())
            .collect(Collectors.joining("\n"))
        );
    }

    private String getRelationship(ArcType relationshipType) {
        return switch (relationshipType) {
            case EXTENSION -> "--|>";
            case AGGREGATION -> "--o";
            case DEPENDENCY -> "..>";
            case IMPLEMENTATION -> "..|>";
            default -> "-->"; // ASSOCIATION
            //case SELFREFERENCE: // SELF-REFERENCING
            //	return "";		// A -- A , SAME CLASS WITH -- IN BETWEEN
            //case COMPOSITION:
            //	return "--*";
        };
    }

}
