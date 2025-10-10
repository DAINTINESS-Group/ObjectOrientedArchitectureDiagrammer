package gr.uoi.ooad.model.diagram.graphml;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.VertexType;
import org.javatuples.Pair;

public class GraphMLClassifierVertex {

    private static final String CLASS_COLOR = "#FF9900";
    private static final String INTERFACE_COLOR = "#3366FF";

    private final StringBuilder graphMLBuffer;

    public GraphMLClassifierVertex() {
        graphMLBuffer = new StringBuilder();
    }

    public StringBuilder convertSinkVertex(ClassDiagram classDiagram) {
        for (Map.Entry<ClassifierVertex, Integer> sinkVertex :
                classDiagram.getGraphNodes().entrySet()) {
            graphMLBuffer.append(
                    GraphMLSyntax.getInstance()
                            .getGraphMLSinkVertexSyntax(
                                    getSinkVertexDescription(
                                            sinkVertex.getKey(),
                                            sinkVertex.getValue(),
                                            classDiagram
                                                    .getGraphMLDiagramGeometry()
                                                    .get(sinkVertex.getValue()))));
        }
        return graphMLBuffer;
    }

    private List<String> getSinkVertexDescription(
            ClassifierVertex classifierVertex, int nodeId, Pair<Double, Double> nodeGeometry) {
        return Arrays.asList(
                String.valueOf(nodeId),
                getSinkVertexColor(classifierVertex),
                classifierVertex.getName(),
                getSinkVertexFields(classifierVertex),
                getSinkVertexMethods(classifierVertex),
                String.valueOf(nodeGeometry.getValue0()),
                String.valueOf(nodeGeometry.getValue1()));
    }

    private String getSinkVertexMethods(ClassifierVertex classifierVertex) {
        if (classifierVertex.getMethods().isEmpty()) {
            return "";
        }
        return classifierVertex.getMethods().stream()
                .map(it -> it.returnType() + " " + it.name())
                .collect(Collectors.joining("\n"));
    }

    private String getSinkVertexFields(ClassifierVertex classifierVertex) {
        if (classifierVertex.getFields().isEmpty()) {
            return "";
        }
        return classifierVertex.getFields().stream()
                .map(it -> String.join(" ", it.type(), it.name()))
                .collect(Collectors.joining("\n"));
    }

    private String getSinkVertexColor(ClassifierVertex leafNode) {
        return leafNode.getVertexType().equals(VertexType.INTERFACE)
                ? INTERFACE_COLOR
                : CLASS_COLOR;
    }
}
