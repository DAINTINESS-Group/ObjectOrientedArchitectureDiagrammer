package model.graphml;

import manager.PackageDiagramManager;
import manager.SourceProject;
import model.diagram.graphml.GraphMLPackageVertexArc;
import model.graph.Arc;
import model.graph.PackageVertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphMLPackageVertexArcTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertVertexArcsToGraphMLTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.convertTreeToDiagram(List.of(
                "src.view",
                "src.model",
                "src.model.strategies",
                "src.controller.commands",
                "src.controller"
            ));
            GraphMLPackageVertexArc graphMLPackageVertexArc = new GraphMLPackageVertexArc(packageDiagramManager.getPackageDiagram());
            StringBuilder actual = graphMLPackageVertexArc.convertVertexArc();

            StringBuilder expected = new StringBuilder();
            List<Arc<PackageVertex>> arcs = new ArrayList<>();
            for (Set<Arc<PackageVertex>> arcSet: packageDiagramManager.getPackageDiagram().getDiagram().values()) {
                arcs.addAll(arcSet);
            }
            int edgeId = 0;

            for (Arc<PackageVertex> e: arcs) {
                expected.append(
                    String.format("    <edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
                            "      <data key=\"d9\"/>\n" +
                            "      <data key=\"d10\">\n" +
                            "        <y:PolyLineEdge>\n" +
                            "          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
                            "          <y:LineStyle color=\"#000000\" type=\"dashed\" width=\"1.0\"/>\n" +
                            "          <y:Arrows source=\"none\" target=\"plain\"/>\n" +
                            "          <y:BendStyle smoothed=\"false\"/>\n" +
                            "        </y:PolyLineEdge>\n" +
                            "      </data>\n" +
                            "    </edge>", edgeId, packageDiagramManager.getPackageDiagram().getGraphNodes().get(e.getSourceVertex()),
                            packageDiagramManager.getPackageDiagram().getGraphNodes().get(e.getTargetVertex())));
                edgeId++;
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
