package model.diagram.plantuml;

import model.graph.Arc;
import model.graph.ArcType;
import model.graph.SinkVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlantUMLSinkVertexArc {

	private final Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;

	public PlantUMLSinkVertexArc(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
		this.diagram = diagram;
	}

	public StringBuilder convertSinkVertexArc() {
		List<Arc<SinkVertex>> arcs = new ArrayList<>();
		for (Set<Arc<SinkVertex>> arcSet: diagram.values()) {
			arcs.addAll(arcSet);
		}

		return new StringBuilder(
			arcs.stream()
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
