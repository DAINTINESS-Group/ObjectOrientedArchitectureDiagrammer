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
            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getClassDiagram().getGraphEdges();

            GraphMLSinkVertexArc graphMLSinkVertexArc = new GraphMLSinkVertexArc(graphNodes);
            StringBuilder actual = graphMLSinkVertexArc.convertSinkVertexArc(graphEdges);

            StringBuilder expected = new StringBuilder();
            List<Arc<SinkVertex>> relationships = new ArrayList<>();
            for (SinkVertex l: sourceProject.getVertices().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands")).getSinkVertices()) {
                for (Arc<SinkVertex> relationship: l.getArcs()) {
                    if (isRelationshipBetweenSamePackages(sourceProject.getVertices(), relationship.getTargetVertex())) {
                        continue;
                    }
                    relationships.add(relationship);
                }
            }
            for (Map.Entry<Arc<SinkVertex>, Integer> e: graphEdges.entrySet()) {
                String edgesStart = e.getKey().getSourceVertex().getName();
                String edgesEnd = e.getKey().getTargetVertex().getName();
                for (Arc<SinkVertex> relationship: relationships) {
                    if (relationship.getSourceVertex().getName().equals(edgesStart) && relationship.getTargetVertex().getName().equals(edgesEnd)) {
                        expected.append(String.format("<edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
                                        "      <data key=\"d10\">\n" +
                                        "        <y:PolyLineEdge>\n" +
                                        "          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
                                        "          <y:LineStyle color=\"#000000\" type=\"%s\" width=\"1.0\"/>\n" +
                                        "          <y:Arrows source=\"%s\" target=\"%s\"/>\n" +
                                        "          <y:BendStyle smoothed=\"false\"/>\n" +
                                        "        </y:PolyLineEdge>\n" +
                                        "      </data>\n" +
                                        "    </edge>\n", e.getValue(), graphNodes.get(e.getKey().getSourceVertex()),
                                graphNodes.get(e.getKey().getTargetVertex()), getEdgesDescription(relationship).get(0),
                                getEdgesDescription(relationship).get(1),getEdgesDescription(relationship).get(2)));
                    }
                }
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isRelationshipBetweenSamePackages(Map<Path, Vertex> vertices, SinkVertex sinkVertex) throws IOException {
        Optional<SinkVertex> any = vertices.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                        "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands")).getSinkVertices().stream()
                .filter(sinkVertex1 -> sinkVertex1.getName().equals(sinkVertex.getName())).findAny();
        return any.isEmpty();
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
