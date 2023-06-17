package model.diagram.graphml;

import model.diagram.PackageDiagram;
import model.graph.Arc;
import model.graph.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GraphMLVertexArc {
    private final StringBuilder graphMLBuffer;
    private final PackageDiagram packageDiagram;

    public GraphMLVertexArc(PackageDiagram packageDiagram) {
        this.packageDiagram = packageDiagram;
        graphMLBuffer = new StringBuilder();
    }

    public StringBuilder convertVertexArc() {
        List<Arc<Vertex>> arcs = new ArrayList<>();
        for (Set<Arc<Vertex>> arcSet: packageDiagram.getDiagram().values()) {
            arcs.addAll(arcSet);
        }

        int edgeId = 0;
        for (Arc<Vertex> arc: arcs) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLVertexArcSyntax(getVertexArcProperties(arc, edgeId)));
            edgeId++;
        }
        return graphMLBuffer;
    }

    private List<String> getVertexArcProperties(Arc<Vertex> relationship, Integer edgeId) {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(packageDiagram.getGraphNodes().get(relationship.getSourceVertex())),
                String.valueOf(packageDiagram.getGraphNodes().get(relationship.getTargetVertex())));
    }

}
