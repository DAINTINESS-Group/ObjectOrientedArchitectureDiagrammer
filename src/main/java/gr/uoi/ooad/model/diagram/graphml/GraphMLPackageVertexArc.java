package gr.uoi.ooad.model.diagram.graphml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.PackageVertex;

public class GraphMLPackageVertexArc {
    private final StringBuilder graphMLBuffer;

    public GraphMLPackageVertexArc() {
        graphMLBuffer = new StringBuilder();
    }

    public StringBuilder convertVertexArc(PackageDiagram packageDiagram) {
        List<Arc<PackageVertex>> arcs =
                packageDiagram.getDiagram().values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(ArrayList::new));

        int edgeId = 0;
        for (Arc<PackageVertex> arc : arcs) {
            graphMLBuffer.append(
                    GraphMLSyntax.getInstance()
                            .getGraphMLVertexArcSyntax(
                                    getVertexArcProperties(packageDiagram, arc, edgeId++)));
        }

        return graphMLBuffer;
    }

    private List<String> getVertexArcProperties(
            PackageDiagram packageDiagram, Arc<PackageVertex> relationship, int edgeId) {
        return Arrays.asList(
                String.valueOf(edgeId),
                String.valueOf(packageDiagram.getGraphNodes().get(relationship.sourceVertex())),
                String.valueOf(packageDiagram.getGraphNodes().get(relationship.targetVertex())));
    }
}
