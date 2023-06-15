package model.graphml;

import manager.PackageDiagramManager;
import manager.SourceProject;
import model.diagram.graphml.GraphMLVertexArc;
import model.graph.Arc;
import model.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphMLVertexArcTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertVertexArcsToGraphMLTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.createDiagram(List.of(
                "src.view",
                "src.model",
                "src.model.strategies",
                "src.controller.commands",
                "src.controller"
            ));
            Map<Vertex, Integer> graphNodes = packageDiagramManager.getDiagram().getGraphNodes();
            Map<Arc<Vertex>, Integer> graphEdges = packageDiagramManager.getDiagram().getGraphEdges();
            GraphMLVertexArc graphMLVertexArc = new GraphMLVertexArc(graphNodes);
            StringBuilder actual = graphMLVertexArc.convertVertexArc(graphEdges);

            StringBuilder expected = new StringBuilder();
            List<Arc<Vertex>> relationships = new ArrayList<>();
            for (Vertex packageNode : sourceProject.getVertices().values()) {
                relationships.addAll(packageNode.getArcs());
            }

            for (Map.Entry<Arc<Vertex>, Integer> e : graphEdges.entrySet()) {
                String edgesStart = e.getKey().getSourceVertex().getName();
                String edgesEnd = e.getKey().getTargetVertex().getName();
                for (Arc<Vertex> relationship : relationships) {
                    if (relationship.getSourceVertex().getName().equals(edgesStart) && relationship.getTargetVertex().getName().equals(edgesEnd)) {
                        expected.append(String.format("    <edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
                                        "      <data key=\"d9\"/>\n" +
                                        "      <data key=\"d10\">\n" +
                                        "        <y:PolyLineEdge>\n" +
                                        "          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
                                        "          <y:LineStyle color=\"#000000\" type=\"dashed\" width=\"1.0\"/>\n" +
                                        "          <y:Arrows source=\"none\" target=\"plain\"/>\n" +
                                        "          <y:BendStyle smoothed=\"false\"/>\n" +
                                        "        </y:PolyLineEdge>\n" +
                                        "      </data>\n" +
                                        "    </edge>", e.getValue(), graphNodes.get(e.getKey().getSourceVertex()),
                                graphNodes.get(e.getKey().getTargetVertex())));
                    }
                }
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
