package model.graphml;

import manager.ClassDiagramManager;
import model.diagram.graphml.GraphMLClassifierVertexArc;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphMLSinkPackageVertexArcTest {

	Path currentDirectory = Path.of(".");

	@Test
	void convertSinkVertexArcsToGraphMLTest() {
		try {
			ClassDiagramManager classDiagramManager = new ClassDiagramManager();
			classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			classDiagramManager.convertTreeToDiagram(List.of("AddLatexCommand", "ChangeVersionsStrategyCommand", "Command", "CommandFactory",
					"CreateCommand", "DisableVersionsManagementCommand", "EditCommand", "EnableVersionsManagementCommand",
					"LoadCommand", "RollbackToPreviousVersionCommand", "SaveCommand"));

			GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc(classDiagramManager.getClassDiagram());
			StringBuilder actual = graphMLClassifierVertexArc.convertSinkVertexArc();

			StringBuilder expected = new StringBuilder();
			List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
			for (Set<Arc<ClassifierVertex>> arcSet: classDiagramManager.getClassDiagram().getDiagram().values()) {
				arcs.addAll(arcSet);
			}
			int edgeId = 0;
			for (Arc<ClassifierVertex> e: arcs) {
				expected.append(
						String.format("<edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
								"      <data key=\"d10\">\n" +
								"        <y:PolyLineEdge>\n" +
								"          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
								"          <y:LineStyle color=\"#000000\" type=\"%s\" width=\"1.0\"/>\n" +
								"          <y:Arrows source=\"%s\" target=\"%s\"/>\n" +
								"          <y:BendStyle smoothed=\"false\"/>\n" +
								"        </y:PolyLineEdge>\n" +
								"      </data>\n" +
								"    </edge>\n", edgeId, classDiagramManager.getClassDiagram().getGraphNodes().get(e.getSourceVertex()),
								classDiagramManager.getClassDiagram().getGraphNodes().get(e.getTargetVertex()), getEdgesDescription(e).get(0),
								getEdgesDescription(e).get(1),getEdgesDescription(e).get(2)));
				edgeId++;
			}
			assertEquals(expected.toString(), actual.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> getEdgesDescription(Arc<ClassifierVertex> relationship) {
		return Arrays.asList(identifyEdgeType(relationship).get(0),
				identifyEdgeType(relationship).get(1), identifyEdgeType(relationship).get(2));
	}
	private List<String> identifyEdgeType(Arc<ClassifierVertex> relationship){
		return switch (relationship.getArcType()) {
		case DEPENDENCY -> Arrays.asList("dashed", "none", "plain");
		case AGGREGATION -> Arrays.asList("line", "white_diamond", "none");
		case ASSOCIATION -> Arrays.asList("line", "none", "standard");
		case EXTENSION -> Arrays.asList("line", "none", "white_delta");
		default -> Arrays.asList("dashed", "none", "white_delta");
		};
	}
}
