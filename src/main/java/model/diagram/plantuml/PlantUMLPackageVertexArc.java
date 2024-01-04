package model.diagram.plantuml;

import model.diagram.PackageDiagram;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.PackageVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlantUMLPackageVertexArc {

	private final PackageDiagram packageDiagram;

	public PlantUMLPackageVertexArc(PackageDiagram diagram) {
		this.packageDiagram = diagram;
	}

	public StringBuilder convertVertexArc() {
		return new StringBuilder(packageDiagram.getDiagram().values()
								 .stream()
								 .flatMap(vertexArcStream -> vertexArcStream
										  .stream()
										  .map(vertexArc ->
											   String.join(" ",
														   vertexArc.sourceVertex().getName(),
														   getRelationship(vertexArc.arcType()),
														   vertexArc.targetVertex().getName())))
									 	  .collect(Collectors.joining("\n")));
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
