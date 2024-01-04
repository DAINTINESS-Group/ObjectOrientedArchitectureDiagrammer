package model.graphml;

import manager.ClassDiagramManager;
import model.diagram.graphml.GraphMLClassifierVertex;
import model.graph.ClassifierVertex;
import model.graph.VertexType;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphMLSinkPackageVertexTest {

	@Test
	void convertSinkVerticesToGraphMLTest() {
		ClassDiagramManager classDiagramManager = new ClassDiagramManager();
		classDiagramManager.createSourceProject(Paths.get(String.format("%s%s%s",
																		PathConstructor.getCurrentPath(),
																		File.separator,
																		PathConstructor.constructPath("src",
																									  "test",
																									  "resources",
																									  "LatexEditor",
																									  "src"))));
		classDiagramManager.convertTreeToDiagram(List.of("AddLatexCommand",
														 "ChangeVersionsStrategyCommand",
														 "Command",
														 "CommandFactory",
														 "CreateCommand",
														 "DisableVersionsManagementCommand",
														 "EditCommand",
														 "EnableVersionsManagementCommand",
														 "LoadCommand",
														 "RollbackToPreviousVersionCommand",
														 "SaveCommand"));
		classDiagramManager.getClassDiagram().setGraphMLDiagramGeometry(Map.ofEntries(Map.entry(0, new Pair<>(10.0, 10.0)),
																					  Map.entry(1, new Pair<>(10.0, 10.0)),
																					  Map.entry(2, new Pair<>(10.0, 10.0)),
																					  Map.entry(3, new Pair<>(10.0, 10.0)),
																					  Map.entry(4, new Pair<>(10.0, 10.0)),
																					  Map.entry(5, new Pair<>(10.0, 10.0)),
																					  Map.entry(6, new Pair<>(10.0, 10.0)),
																					  Map.entry(7, new Pair<>(10.0, 10.0)),
																					  Map.entry(8, new Pair<>(10.0, 10.0)),
																					  Map.entry(9, new Pair<>(10.0, 10.0)),
																					  Map.entry(10, new Pair<>(10.0, 10.0))));

		GraphMLClassifierVertex graphMLClassifierVertex = new GraphMLClassifierVertex(classDiagramManager.getClassDiagram());
		StringBuilder 			actual					= graphMLClassifierVertex.convertSinkVertex();

		StringBuilder expected = new StringBuilder();
		for (ClassifierVertex leafNode: classDiagramManager.getClassDiagram().getGraphNodes().keySet()) {
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
										  "    </node>\n",
										  classDiagramManager.getClassDiagram().getGraphNodes().get(leafNode),
										  10.0,
										  10.0,
										  getNodesColor(leafNode),
										  leafNode.getName(),
										  getNodesFields(leafNode),
										  getNodesMethods(leafNode)));
		}
		assertEquals(expected.toString(), actual.toString());
	}

	private String getNodesFields(ClassifierVertex l) {
		if (l.getFields().isEmpty()) {
			return "";
		}
		StringBuilder fields = new StringBuilder();
		for (ClassifierVertex.Field field: l.getFields()) {
			fields.append(field.type()).append(" ").append(field.name()).append("\n");
		}
		return fields.deleteCharAt(fields.length() - 1).toString();
	}

	private String getNodesMethods(ClassifierVertex l) {
		if ((l).getMethods().isEmpty()) {
			return "";
		}
		StringBuilder methods = new StringBuilder();
		for (ClassifierVertex.Method method: l.getMethods()) {
			methods.append(method.returnType()).append(" ").append(method.name()).append("\n");
		}
		return methods.deleteCharAt(methods.length() - 1).toString();
	}

	private String getNodesColor(ClassifierVertex l) {
		if (l.getVertexType().equals(VertexType.INTERFACE)) {
			return "#3366FF";
		}
		return "#FF9900";
	}

}
