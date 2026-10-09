package gr.uoi.ooad.model.diagram.plantuml;

import java.util.Set;
import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;

public class PlantUMLClassifierVertexArc {

    public static StringBuilder convertSinkVertexArcs(ClassDiagram classDiagram) {
        StringBuilder ret = new StringBuilder();
        for (Set<Arc<ClassifierVertex>> arcs : classDiagram.getDiagram().values()) {
            for (Arc<ClassifierVertex> it : arcs) {
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
