package model.diagram.plantuml;

import model.diagram.ClassDiagram;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.ClassifierVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlantUMLClassifierVertexArc
{

    private final ClassDiagram classDiagram;


    public PlantUMLClassifierVertexArc(ClassDiagram diagram)
    {
        this.classDiagram = diagram;
    }


    public StringBuilder convertSinkVertexArc()
    {
        return new StringBuilder(classDiagram
                                     .getDiagram()
                                     .values()
                                     .stream()
                                     .flatMap(sinkVertexStream -> sinkVertexStream
                                         .stream()
                                             .map(sinkVertexArc -> String.join(" ",
                                                                               sinkVertexArc.sourceVertex().getName(),
                                                                               getRelationship(sinkVertexArc.arcType()),
                                                                               sinkVertexArc.targetVertex().getName())))
                                             .collect(Collectors.joining("\n")));
    }


    private String getRelationship(ArcType relationshipType)
    {
        return switch (relationshipType)
        {
            case EXTENSION      -> "--|>";
            case AGGREGATION    -> "o--";
            case DEPENDENCY     -> "..>";
            case IMPLEMENTATION -> "..|>";
            // ASSOCIATION
            default             -> "-->";
        };
    }

}
