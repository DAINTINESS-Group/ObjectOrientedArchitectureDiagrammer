package model.diagram.plantuml;

import model.diagram.ClassDiagram;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.ClassifierVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlantUMLClassifierVertexArc {


	private final ClassDiagram classDiagram;

	public PlantUMLClassifierVertexArc(ClassDiagram diagram) {
		classDiagram = diagram;
	}

	public StringBuilder convertSinkVertexArc() {
		List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
		for (Set<Arc<ClassifierVertex>> arcSet: classDiagram.getDiagram().values()) {
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
			case AGGREGATION -> "o--";
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
