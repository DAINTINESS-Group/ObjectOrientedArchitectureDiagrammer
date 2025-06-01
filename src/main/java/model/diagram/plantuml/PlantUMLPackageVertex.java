package model.diagram.plantuml;

import java.util.stream.Collectors;
import model.diagram.PackageDiagram;

public class PlantUMLPackageVertex {

    public static StringBuilder convertVertices(PackageDiagram packageDiagram) {
        return new StringBuilder(
                packageDiagram.getDiagram().keySet().stream()
                        .map(it -> it.getVertexType() + " " + it.getName() + " {\n" + "}\n")
                        .collect(Collectors.joining("\n")));
    }
}
