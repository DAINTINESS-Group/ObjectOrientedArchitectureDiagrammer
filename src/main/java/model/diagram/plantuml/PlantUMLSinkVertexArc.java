package model.diagram.plantuml;

import model.graph.Arc;
import model.graph.ArcType;
import model.graph.SinkVertex;

import java.util.Map;
import java.util.stream.Collectors;

public class PlantUMLSinkVertexArc {

	private final Map<Arc<SinkVertex>, Integer> graphEdges;

	public PlantUMLSinkVertexArc(Map<Arc<SinkVertex>, Integer> graphEdges) {
		this.graphEdges = graphEdges;
	}

	public StringBuilder convertSinkVertexArc() {
		return new StringBuilder(
			graphEdges.keySet().stream()
				.map(sinkVertexArc ->
					sinkVertexArc.getSourceVertex().getName() + " " + getRelationship(sinkVertexArc.getArcType()) + " " +
					sinkVertexArc.getTargetVertex().getName())
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
