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
		this.classDiagram = diagram;
	}

	public StringBuilder convertSinkVertexArc() {
		List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
		for (Set<Arc<ClassifierVertex>> arcSet: this.classDiagram.getDiagram().values()) {
			arcs.addAll(arcSet);
		}

		return
			new StringBuilder(arcs
				.stream()
				.map(sinkVertexArc ->
					sinkVertexArc.sourceVertex().getName() + " " + getRelationship(sinkVertexArc.arcType()) + " " +
					sinkVertexArc.targetVertex().getName())
				.collect(Collectors.joining("\n"))
		);
	}

	private String getRelationship(ArcType relationshipType) {
		return switch (relationshipType) {
			case EXTENSION 		-> "--|>";
			case AGGREGATION 	-> "o--";
			case DEPENDENCY 	-> "..>";
			case IMPLEMENTATION -> "..|>";
			// ASSOCIATION
			default 			-> "-->";
		};
	}

}
