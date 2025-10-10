package gr.uoi.ooad.model.diagram.graphml;

import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.graph.PackageVertex;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.javatuples.Pair;

public class GraphMLPackageVertex {

    private final StringBuilder graphMLBuffer;

    public GraphMLPackageVertex() {
        graphMLBuffer = new StringBuilder();
    }

    public StringBuilder convertVertex(PackageDiagram packageDiagram) {
        for (Map.Entry<PackageVertex, Integer> entry : packageDiagram.getGraphNodes().entrySet()) {
            graphMLBuffer.append(
                    GraphMLSyntax.getInstance()
                            .getGraphMLVertexSyntax(
                                    getVertexDescription(
                                            entry.getKey(),
                                            entry.getValue(),
                                            packageDiagram
                                                    .getGraphMLDiagramGeometry()
                                                    .get(entry.getValue()))));
        }
        return graphMLBuffer;
    }

    private List<String> getVertexDescription(
            PackageVertex packageNode, int nodeId, Pair<Double, Double> nodeGeometry) {
        return Arrays.asList(
                String.valueOf(nodeId),
                packageNode.getName(),
                String.valueOf(nodeGeometry.getValue0()),
                String.valueOf(nodeGeometry.getValue1()));
    }
}
