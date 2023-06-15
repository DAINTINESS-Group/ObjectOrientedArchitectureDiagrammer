package model.graphml;

import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.graphml.GraphMLSinkVertex;
import model.graph.SinkVertex;
import model.graph.VertexType;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphMLSinkVertexTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertSinkVerticesToGraphMLTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.convertTreeToDiagram(List.of("AddLatexCommand", "ChangeVersionsStrategyCommand", "Command", "CommandFactory",
                    "CreateCommand", "DisableVersionsManagementCommand", "EditCommand", "EnableVersionsManagementCommand",
                    "LoadCommand", "RollbackToPreviousVersionCommand", "SaveCommand"));
            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();
            GraphMLSinkVertex graphMLSinkVertex = new GraphMLSinkVertex(graphNodes, Map.ofEntries(
                    Map.entry(0, new Pair<>(10.0, 10.0)),
                    Map.entry(1, new Pair<>(10.0, 10.0)),
                    Map.entry(2, new Pair<>(10.0, 10.0)),
                    Map.entry(3, new Pair<>(10.0, 10.0)),
                    Map.entry(4, new Pair<>(10.0, 10.0)),
                    Map.entry(5, new Pair<>(10.0, 10.0)),
                    Map.entry(6, new Pair<>(10.0, 10.0)),
                    Map.entry(7, new Pair<>(10.0, 10.0)),
                    Map.entry(8, new Pair<>(10.0, 10.0)),
                    Map.entry(9, new Pair<>(10.0, 10.0)),
                    Map.entry(10, new Pair<>(10.0, 10.0))
            ));
            StringBuilder actual = graphMLSinkVertex.convertSinkVertex();

            StringBuilder expected = new StringBuilder();
            for (SinkVertex leafNode: graphNodes.keySet()) {
                expected.append(String.format("    <node id=\"n%s\">\n" +
                                "      <data key=\"d4\" xml:space=\"preserve\"/>\n" +
                                "      <data key=\"d5\"/>\n" +
                                "      <data key=\"d6\">\n" +
                                "        <y:UMLClassNode>\n" +
                                "          <y:Geometry height=\"100.0\" width=\"150.0\" x=\"%s\" y=\"%s\"/>\n" +
                                "          <y:Fill color=\"%s\" transparent=\"false\"/>\n" +
                                "          <y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>\n" +
                                "          <y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"13\" fontStyle=\"bold\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"19.92626953125\" horizontalTextPosition=\"center\" iconTextGap=\"4\" modelName=\"custom\" textColor=\"#000000\" verticalTextPosition=\"bottom\" visible=\"true\" width=\"79.14990234375\" x=\"10.425048828125\" xml:space=\"preserve\" y=\"3.0\">%s<y:LabelModel><y:SmartNodeLabelModel distance=\"4.0\"/></y:LabelModel><y:ModelParameter><y:SmartNodeLabelModelParameter labelRatioX=\"0.0\" labelRatioY=\"0.0\" nodeRatioX=\"0.0\" nodeRatioY=\"0.0\" offsetX=\"0.0\" offsetY=\"0.0\" upX=\"0.0\" upY=\"-1.0\"/></y:ModelParameter></y:NodeLabel>\n" +
                                "          <y:UML clipContent=\"true\" constraint=\"\" hasDetailsColor=\"false\" omitDetails=\"false\" stereotype=\"\" use3DEffect=\"true\">\n" +
                                "            <y:AttributeLabel xml:space=\"preserve\">%s</y:AttributeLabel>\n" +
                                "            <y:MethodLabel xml:space=\"preserve\">%s</y:MethodLabel>\n" +
                                "          </y:UML>\n" +
                                "        </y:UMLClassNode>\n" +
                                "      </data>\n" +
                                "    </node>\n", graphNodes.get(leafNode), 10.0, 10.0, getNodesColor(leafNode), leafNode.getName(),
                        getNodesFields(leafNode), getNodesMethods(leafNode)));
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNodesFields(SinkVertex l) {
        if (l.getFields().size() == 0) {
            return "";
        }
        StringBuilder fields = new StringBuilder();
        for (SinkVertex.Field field: l.getFields()) {
            fields.append(field.getType()).append(" ").append(field.getName()).append("\n");
        }
        return fields.deleteCharAt(fields.length() - 1).toString();
    }

    private String getNodesMethods(SinkVertex l) {
        if ((l).getMethods().size() == 0) {
            return "";
        }
        StringBuilder methods = new StringBuilder();
        for (SinkVertex.Method method: l.getMethods()) {
            methods.append(method.getReturnType()).append(" ").append(method.getName()).append("\n");
        }
        return methods.deleteCharAt(methods.length() - 1).toString();
    }

    private String getNodesColor(SinkVertex l) {
        if (l.getVertexType().equals(VertexType.INTERFACE)) {
            return "#3366FF";
        }
        return "#FF9900";
    }

}
