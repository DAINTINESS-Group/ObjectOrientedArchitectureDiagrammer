package model.diagram.plantuml;

import model.diagram.ClassDiagram;
import model.graph.ArcType;

import java.util.Collection;
import java.util.stream.Collectors;

public class PlantUMLClassifierVertexArc
{


    public static StringBuilder convertSinkVertexArcs(ClassDiagram classDiagram)
    {
        return new StringBuilder(classDiagram.getDiagram().values().stream()
                                     .flatMap(Collection::stream)
                                     .map(it -> String.join(" ",
                                                                       it.sourceVertex().getName(),
                                                                       getRelationship(it.arcType()),
                                                                       it.targetVertex().getName()))
                                     .collect(Collectors.joining("\n")));
    }


    private static String getRelationship(ArcType relationshipType)
    {
        return switch (relationshipType)
        {
            case EXTENSION      -> "--|>";
            case AGGREGATION    -> "o--";
            case DEPENDENCY     -> "..>";
            case IMPLEMENTATION -> "..|>";
            // ASSOCIATION.
            default             -> "-->";
        };
    }

}
