package model.diagram.plantuml;

import java.util.Set;
import model.diagram.PackageDiagram;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.PackageVertex;

public class PlantUMLPackageVertexArc {

    public static StringBuilder convertVertexArcs(PackageDiagram packageDiagram) {
        StringBuilder ret = new StringBuilder();
        for (Set<Arc<PackageVertex>> arcs : packageDiagram.getDiagram().values()) {
            for (Arc<PackageVertex> it : arcs) {
                ret.append(it.sourceVertex().getName())
                        .append(" ")
                        .append(getRelationship(it.arcType()))
                        .append(" ")
                        .append(it.targetVertex().getName())
                        .append(System.lineSeparator());
            }
        }
        return ret;
    }

    private static String getRelationship(ArcType relationshipType) {
        return switch (relationshipType) {
            case EXTENSION -> "--|>";
            case AGGREGATION -> "o--";
            case DEPENDENCY -> "..>";
            case IMPLEMENTATION -> "..|>";
            case ASSOCIATION -> "-->";
        };
    }
}
