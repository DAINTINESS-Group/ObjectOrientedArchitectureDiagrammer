package model.diagram.graphml;

import model.diagram.PackageDiagram;
import model.graph.Vertex;
import org.javatuples.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphMLVertex {

    private final StringBuilder graphMLBuffer;
    private final PackageDiagram packageDiagram;


    public GraphMLVertex(PackageDiagram packageDiagram) {
        this.packageDiagram = packageDiagram;
        this.graphMLBuffer = new StringBuilder();
    }

    public StringBuilder convertVertex() {
        for (Map.Entry<Vertex, Integer> entry: packageDiagram.getGraphNodes().entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLVertexSyntax(getVertexDescription(entry.getKey(),
                    entry.getValue(), packageDiagram.getDiagramGeometry().get(entry.getValue()))));
        }
        return graphMLBuffer;
    }

    private List<String> getVertexDescription(Vertex packageNode, int nodeId, Pair<Double, Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), packageNode.getName(), String.valueOf(nodeGeometry.getValue0()),
                String.valueOf(nodeGeometry.getValue1()));
    }

}
