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
        packageDiagram = diagram;
    }

    public StringBuilder convertVertexArc() {
        List<Arc<PackageVertex>> arcs = new ArrayList<>();
        for (Set<Arc<PackageVertex>> arcSet: packageDiagram.getDiagram().values()) {
            arcs.addAll(arcSet);
        }

        return new StringBuilder(
        arcs.stream()
            .map(vertexArc ->
                vertexArc.getSourceVertex().getName() + " " + getRelationship(vertexArc.getArcType()) + " " +
                vertexArc.getTargetVertex().getName())
            .collect(Collectors.joining("\n"))
        );
    }

    private String getRelationship(ArcType relationshipType) {
        return switch (relationshipType) {
            case EXTENSION -> "--|>";
            case AGGREGATION -> "o--";
            case DEPENDENCY -> "..>";
            case IMPLEMENTATION -> "..|>";
            default -> "-->"; // ASSOCIATION
            //case SELFREFERENCE: // SELF-REFERENCING
            //	return "";		// A -- A , SAME CLASS WITH -- IN BETWEEN
            //case COMPOSITION:
            //	return "--*";
        };
    }

}
