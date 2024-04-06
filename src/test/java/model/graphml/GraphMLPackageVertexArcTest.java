package model.graphml;

import manager.PackageDiagramManager;
import model.diagram.graphml.GraphMLPackageVertexArc;
import model.graph.Arc;
import model.graph.PackageVertex;
import org.junit.jupiter.api.Test;
import utils.PathTemplate.LatexEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphMLPackageVertexArcTest
{

    @Test
    void convertVertexArcsToGraphMLTest()
    {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(LatexEditor.SRC.path);
        packageDiagramManager.convertTreeToDiagram(List.of("src.view",
                                                           "src.model",
                                                           "src.model.strategies",
                                                           "src.controller.commands",
                                                           "src.controller"));
        GraphMLPackageVertexArc graphMLPackageVertexArc = new GraphMLPackageVertexArc();
        StringBuilder           actual                  = graphMLPackageVertexArc.convertVertexArc(packageDiagramManager.getPackageDiagram());

        StringBuilder            expected = new StringBuilder();
        List<Arc<PackageVertex>> arcs     = packageDiagramManager.getPackageDiagram().getDiagram().values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));

        int edgeId = 0;
        for (Arc<PackageVertex> e : arcs)
        {
            expected.append(String.format("""
                                                  <edge id="e%s" source="n%s" target="n%s">
                                                    <data key="d9"/>
                                                    <data key="d10">
                                                      <y:PolyLineEdge>
                                                        <y:Path sx="0.0" sy="0.0" tx="0.0" ty="0.0"/>
                                                        <y:LineStyle color="#000000" type="dashed" width="1.0"/>
                                                        <y:Arrows source="none" target="plain"/>
                                                        <y:BendStyle smoothed="false"/>
                                                      </y:PolyLineEdge>
                                                    </data>
                                                  </edge>\
                                              """,
                                          edgeId++,
                                          packageDiagramManager.getPackageDiagram().getGraphNodes().get(e.sourceVertex()),
                                          packageDiagramManager.getPackageDiagram().getGraphNodes().get(e.targetVertex())));
        }

        assertEquals(expected.toString(), actual.toString());
    }
}
