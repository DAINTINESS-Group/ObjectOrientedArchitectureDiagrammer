package gr.uoi.ooad.model.graphml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gr.uoi.ooad.manager.ClassDiagramManager;
import gr.uoi.ooad.model.diagram.graphml.GraphMLClassifierVertexArc;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class GraphMLSinkPackageVertexArcTest {

    @Test
    void convertSinkVertexArcsToGraphMLTest() {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(
                List.of(
                        "AddLatexCommand",
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

        GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc();
        StringBuilder actual =
                graphMLClassifierVertexArc.convertSinkVertexArc(
                        classDiagramManager.getClassDiagram());

        StringBuilder expected = new StringBuilder();
        List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
        for (Set<Arc<ClassifierVertex>> arcSet :
                classDiagramManager.getClassDiagram().getDiagram().values()) {
            arcs.addAll(arcSet);
        }
        int edgeId = 0;
        for (Arc<ClassifierVertex> e : arcs) {
            expected.append(
                    String.format(
                            """
                              <edge id="e%s" source="n%s" target="n%s">
                                    <data key="d10">
                                      <y:PolyLineEdge>
                                        <y:Path sx="0.0" sy="0.0" tx="0.0" ty="0.0"/>
                                        <y:LineStyle color="#000000" type="%s" width="1.0"/>
                                        <y:Arrows source="%s" target="%s"/>
                                        <y:BendStyle smoothed="false"/>
                                      </y:PolyLineEdge>
                                    </data>
                                  </edge>
                              """,
                            edgeId,
                            classDiagramManager
                                    .getClassDiagram()
                                    .getGraphNodes()
                                    .get(e.sourceVertex()),
                            classDiagramManager
                                    .getClassDiagram()
                                    .getGraphNodes()
                                    .get(e.targetVertex()),
                            getEdgesDescription(e).get(0),
                            getEdgesDescription(e).get(1),
                            getEdgesDescription(e).get(2)));
            edgeId++;
        }
        assertEquals(expected.toString(), actual.toString());
    }

    private List<String> getEdgesDescription(Arc<ClassifierVertex> relationship) {
        return List.of(
                identifyEdgeType(relationship).get(0),
                identifyEdgeType(relationship).get(1),
                identifyEdgeType(relationship).get(2));
    }

    private List<String> identifyEdgeType(Arc<ClassifierVertex> relationship) {
        return switch (relationship.arcType()) {
            case DEPENDENCY -> List.of("dashed", "none", "plain");
            case AGGREGATION -> List.of("line", "white_diamond", "none");
            case ASSOCIATION -> List.of("line", "none", "standard");
            case EXTENSION -> List.of("line", "none", "white_delta");
            default -> List.of("dashed", "none", "white_delta");
        };
    }
}
