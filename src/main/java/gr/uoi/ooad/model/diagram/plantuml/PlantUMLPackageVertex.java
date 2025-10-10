package gr.uoi.ooad.model.diagram.plantuml;

import gr.uoi.ooad.model.diagram.PackageDiagram;
import java.util.stream.Collectors;

public class PlantUMLPackageVertex {

    public static StringBuilder convertVertices(PackageDiagram packageDiagram) {
        return new StringBuilder(
                packageDiagram.getDiagram().keySet().stream()
                        .map(it -> it.getVertexType() + " " + it.getName() + " {\n" + "}\n")
                        .collect(Collectors.joining("\n")));
    }
}
