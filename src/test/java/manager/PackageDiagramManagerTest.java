package manager;

import model.graph.Arc;
import model.graph.Vertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageDiagramManagerTest {
    Path currentDirectory = Path.of(".");

    @Test
    void populateGraphEdgesTest() {
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
            Set<Arc<Vertex>> expectedArcs = packageDiagramManager.getDiagram().getGraphEdges().keySet();

            List<Arc<Vertex>> actualArcs = new ArrayList<>();
            for (Vertex packageNode : sourceProject.getVertices().values()) {
                actualArcs.addAll(packageNode.getArcs());
            }
            for (Arc<Vertex> arc : expectedArcs) {
                actualArcs.stream()
                    .filter(vertexArc ->
                        vertexArc.getSourceVertex().getName().equals(arc.getSourceVertex().getName()) &&
                        vertexArc.getTargetVertex().getName().equals(arc.getTargetVertex().getName()))
                    .findFirst().orElseGet(Assertions::fail);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void populateGraphMLPackageNodeTest() {
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

            Map<Path, Vertex> packageNodes = sourceProject.getVertices();
            packageNodes.remove(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            assertEquals(packageNodes.size(), graphNodes.size());
            Iterator<Map.Entry<Path, Vertex>> iter1 = packageNodes.entrySet().iterator();
            Iterator<Map.Entry<Vertex, Integer>> iter2 = graphNodes.entrySet().iterator();

            List<String> l1 = new ArrayList<>();
            List<String> l2 = new ArrayList<>();
            while (iter1.hasNext() || iter2.hasNext()) {
                Map.Entry<Path, Vertex> e1 = iter1.next();
                Map.Entry<Vertex, Integer> e2 = iter2.next();
                l1.add(e1.getValue().getName());
                l2.add(e2.getKey().getName());
            }
            Collections.sort(l1);
            Collections.sort(l2);
            assertTrue(l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
