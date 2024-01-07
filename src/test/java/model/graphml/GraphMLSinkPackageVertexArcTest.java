package model.graphml;

import manager.ClassDiagramManager;
import model.diagram.graphml.GraphMLClassifierVertexArc;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphMLSinkPackageVertexArcTest
{

    @Test
    void convertSinkVertexArcsToGraphMLTest()
    {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        classDiagramManager.createSourceProject(Paths.get(String.format("%s%s%s".formatted(PathConstructor.getCurrentPath(),
                                                                                           File.separator,
                                                                                           PathConstructor.constructPath("src",
                                                                                                                         "test",
                                                                                                                         "resources",
                                                                                                                         "LatexEditor",
                                                                                                                         "src")))));
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

        GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc(classDiagramManager.getClassDiagram());
        StringBuilder              actual                     = graphMLClassifierVertexArc.convertSinkVertexArc();

        StringBuilder               expected = new StringBuilder();
        List<Arc<ClassifierVertex>> arcs     = new ArrayList<>();
        for (Set<Arc<ClassifierVertex>> arcSet : classDiagramManager.getClassDiagram().getDiagram().values())
        {
            arcs.addAll(arcSet);
        }
        int edgeId = 0;
        for (Arc<ClassifierVertex> e : arcs)
        {
            expected.append(
                String.format("""
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
                              classDiagramManager.getClassDiagram().getGraphNodes().get(e.sourceVertex()),
                              classDiagramManager.getClassDiagram().getGraphNodes().get(e.targetVertex()),
                              getEdgesDescription(e).get(0),
                              getEdgesDescription(e).get(1),
                              getEdgesDescription(e).get(2)));
            edgeId++;
        }
        assertEquals(expected.toString(), actual.toString());
    }


    private List<String> getEdgesDescription(Arc<ClassifierVertex> relationship)
    {
        return Arrays.asList(identifyEdgeType(relationship).get(0),
                             identifyEdgeType(relationship).get(1), identifyEdgeType(relationship).get(2));
    }


    private List<String> identifyEdgeType(Arc<ClassifierVertex> relationship)
    {
        return switch (relationship.arcType())
        {
            case DEPENDENCY  -> Arrays.asList("dashed", "none", "plain");
            case AGGREGATION -> Arrays.asList("line", "white_diamond", "none");
            case ASSOCIATION -> Arrays.asList("line", "none", "standard");
            case EXTENSION   -> Arrays.asList("line", "none", "white_delta");
            default          -> Arrays.asList("dashed", "none", "white_delta");
        };
    }
}
