package model.graphml;

import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.graphml.GraphMLSinkVertexArc;
import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphMLSinkVertexArcTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertSinkVertexArcsToGraphMLTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.convertTreeToDiagram(List.of("AddLatexCommand", "ChangeVersionsStrategyCommand", "Command", "CommandFactory",
                    "CreateCommand", "DisableVersionsManagementCommand", "EditCommand", "EnableVersionsManagementCommand",
                    "LoadCommand", "RollbackToPreviousVersionCommand", "SaveCommand"));
            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();
            Map<SinkVertex, Set<Arc<SinkVertex>>> diagram = classDiagramManager.getDiagram();

            GraphMLSinkVertexArc graphMLSinkVertexArc = new GraphMLSinkVertexArc(graphNodes, diagram);
            StringBuilder actual = graphMLSinkVertexArc.convertSinkVertexArc();

            StringBuilder expected = new StringBuilder();
            List<Arc<SinkVertex>> arcs = new ArrayList<>();
            for (Set<Arc<SinkVertex>> arcSet: diagram.values()) {
                arcs.addAll(arcSet);
            }
            int edgeId = 0;
            for (Arc<SinkVertex> e: arcs) {
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
                            "    </edge>\n", edgeId, graphNodes.get(e.getSourceVertex()),
                    graphNodes.get(e.getTargetVertex()), getEdgesDescription(e).get(0),
                    getEdgesDescription(e).get(1),getEdgesDescription(e).get(2)));
            edgeId++;
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getEdgesDescription(Arc<SinkVertex> relationship) {
        return Arrays.asList(identifyEdgeType(relationship).get(0),
                identifyEdgeType(relationship).get(1), identifyEdgeType(relationship).get(2));
    }
    private List<String> identifyEdgeType(Arc<SinkVertex> relationship){
        return switch (relationship.getArcType()) {
            case DEPENDENCY -> Arrays.asList("dashed", "none", "plain");
            case AGGREGATION -> Arrays.asList("line", "white_diamond", "none");
            case ASSOCIATION -> Arrays.asList("line", "none", "standard");
            case EXTENSION -> Arrays.asList("line", "none", "white_delta");
            default -> Arrays.asList("dashed", "none", "white_delta");
        };
    }
}
