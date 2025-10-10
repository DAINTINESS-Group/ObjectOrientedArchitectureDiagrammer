package gr.uoi.ooad.model.diagram.plantuml;

import java.util.Collection;
import java.util.stream.Collectors;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.graph.ArcType;

public class PlantUMLPackageVertexArc {

    public static StringBuilder convertVertexArcs(PackageDiagram packageDiagram) {
        return new StringBuilder(
                packageDiagram.getDiagram().values().stream()
                        .flatMap(Collection::stream)
                        .map(
                                it ->
                                        String.join(
                                                " ",
                                                it.sourceVertex().getName(),
                                                getRelationship(it.arcType()),
                                                it.targetVertex().getName()))
                        .collect(Collectors.joining("\n")));
    }

    private static String getRelationship(ArcType relationshipType) {
        return switch (relationshipType) {
            case EXTENSION -> "--|>";
            case AGGREGATION -> "o--";
            case DEPENDENCY -> "..>";
            case IMPLEMENTATION -> "..|>";
                // ASSOCIATION
            default -> "-->";
        };
    }
}
