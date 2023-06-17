package model.diagram.graphml;

import model.diagram.ClassDiagram;
import model.graph.SinkVertex;
import model.graph.VertexType;
import org.javatuples.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphMLSinkVertex {

    private static final String CLASS_COLOR = "#FF9900";
    private static final String INTERFACE_COLOR = "#3366FF";
    private final StringBuilder graphMLBuffer;
    private final ClassDiagram classDiagram;

    public GraphMLSinkVertex(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
        this.graphMLBuffer = new StringBuilder();
    }

    public StringBuilder convertSinkVertex() {
        for (Map.Entry<SinkVertex, Integer> sinkVertex: classDiagram.getGraphNodes().entrySet()) {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLSinkVertexSyntax(
                    getSinkVertexDescription(sinkVertex.getKey(), sinkVertex.getValue(), classDiagram.getDiagramGeometry().get(sinkVertex.getValue()))));
        }
        return graphMLBuffer;
    }

    private List<String> getSinkVertexDescription(SinkVertex sinkVertex, int nodeId, Pair<Double, Double> nodeGeometry) {
        return Arrays.asList(String.valueOf(nodeId), getSinkVertexColor(sinkVertex), sinkVertex.getName(), getSinkVertexFields(sinkVertex),
                getSinkVertexMethods(sinkVertex), String.valueOf(nodeGeometry.getValue0()), String.valueOf(nodeGeometry.getValue1()));
    }

    private String getSinkVertexMethods(SinkVertex sinkVertex) {
        if (sinkVertex.getMethods().size() == 0) {
            return "";
        }
        return sinkVertex.getMethods().stream()
                .map(method -> method.getReturnType() + " " + method.getName())
                .collect(Collectors.joining("\n"));
    }

    private String getSinkVertexFields(SinkVertex sinkVertex) {
        if (sinkVertex.getFields().size() == 0) {
            return "";
        }
        return sinkVertex.getFields().stream()
            .map(field -> field.getType() + " " + field.getName())
            .collect(Collectors.joining("\n"));
    }

    private String getSinkVertexColor(SinkVertex leafNode) {
        if (leafNode.getVertexType().equals(VertexType.INTERFACE)) {
            return INTERFACE_COLOR;
        }
        return CLASS_COLOR;
    }

}
