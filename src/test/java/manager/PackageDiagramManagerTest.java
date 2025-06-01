package manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.diagram.PackageDiagram;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.Interpreter;
import utils.PathTemplate.LatexEditor;

public class PackageDiagramManagerTest {

    @Test
    void createSourceProjectTest() {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        PackageDiagram packageDiagram = packageDiagramManager.getPackageDiagram();

        SourceProject sourceProject = new SourceProject();
        Map<Path, PackageVertex> packageNodes =
                sourceProject.createPackageGraph(LatexEditor.SRC.path, packageDiagram);

        Interpreter interpreter = new Interpreter();
        interpreter.parseProject(LatexEditor.SRC.path);
        interpreter.convertTreeToGraph();

        ArrayList<PackageVertex> interpreterVertices =
                new ArrayList<>(interpreter.getVertices().values());
        assertEquals(packageNodes.size(), interpreterVertices.size());

        for (Map.Entry<Path, PackageVertex> vertexEntry : packageNodes.entrySet()) {
            PackageVertex optionalPackageVertex =
                    interpreterVertices.stream()
                            .filter(
                                    it ->
                                            it.getName().equals(vertexEntry.getValue().getName())
                                                    && it.getParentVertex()
                                                            .getName()
                                                            .equals(
                                                                    vertexEntry
                                                                            .getValue()
                                                                            .getParentVertex()
                                                                            .getName()))
                            .findFirst()
                            .orElseGet(Assertions::fail);

            assertEquals(
                    vertexEntry.getValue().getNeighbourVertices().size(),
                    optionalPackageVertex.getNeighbourVertices().size());

            for (PackageVertex neighbourPackageVertex :
                    vertexEntry.getValue().getNeighbourVertices()) {
                assertTrue(
                        optionalPackageVertex.getNeighbourVertices().stream()
                                .anyMatch(
                                        it ->
                                                it.getName()
                                                        .equals(neighbourPackageVertex.getName())));
            }

            assertEquals(
                    vertexEntry.getValue().getSinkVertices().size(),
                    optionalPackageVertex.getSinkVertices().size());

            for (ClassifierVertex classifierVertex : vertexEntry.getValue().getSinkVertices()) {
                assertTrue(
                        optionalPackageVertex.getSinkVertices().stream()
                                .anyMatch(it -> it.getName().equals(classifierVertex.getName())));
            }
        }
    }

    @Test
    void populateGraphMLPackageNodeTest() {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        PackageDiagram packageDiagram = packageDiagramManager.getPackageDiagram();

        SourceProject sourceProject = new SourceProject();
        Map<Path, PackageVertex> packageNodes =
                sourceProject.createPackageGraph(LatexEditor.SRC.path, packageDiagram);

        packageDiagramManager.convertTreeToDiagram(
                List.of(
                        "src.view",
                        "src.model",
                        "src.model.strategies",
                        "src.controller.commands",
                        "src.controller"));

        Map<PackageVertex, Integer> graphNodes =
                packageDiagramManager.getPackageDiagram().getGraphNodes();
        packageNodes.remove(LatexEditor.SRC.path);

        assertEquals(packageNodes.size(), graphNodes.size());

        List<String> l1 =
                packageNodes.values().stream()
                        .map(PackageVertex::getName)
                        .collect(Collectors.toCollection(ArrayList::new));

        List<String> l2 =
                graphNodes.keySet().stream()
                        .map(PackageVertex::getName)
                        .collect(Collectors.toCollection(ArrayList::new));

        Collections.sort(l1);
        Collections.sort(l2);

        assertEquals(l1.size(), l2.size());
        assertTrue(l1.containsAll(l2));
        assertTrue(l2.containsAll(l1));
    }
}
