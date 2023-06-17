package model.diagram.plantuml;

import model.diagram.PackageDiagram;

import java.util.stream.Collectors;

public class PlantUMLVertex {


    private final PackageDiagram packageDiagram;

    public PlantUMLVertex(PackageDiagram diagram) {
        packageDiagram = diagram;
    }

    public StringBuilder convertVertex() {
        return new StringBuilder(
                packageDiagram.getDiagram().keySet().stream()
                .map(vertex -> vertex.getVertexType().toString().toLowerCase() + " " + vertex.getName() + " {\n" + "}\n")
                .collect(Collectors.joining("\n"))
        );
	}

}
