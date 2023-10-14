package model.diagram.plantuml;

import model.diagram.PackageDiagram;

import java.util.stream.Collectors;

public class PlantUMLPackageVertex {

	private final PackageDiagram packageDiagram;

	public PlantUMLPackageVertex(PackageDiagram diagram) {
		this.packageDiagram = diagram;
	}

	public StringBuilder convertVertex() {
		return new StringBuilder(this.packageDiagram.getDiagram().keySet()
				.stream()
				.map(vertex -> vertex.getVertexType().toString().toLowerCase() + " " + vertex.getName() + " {\n" + "}\n")
				.collect(Collectors.joining("\n"))
		);
	}

}
