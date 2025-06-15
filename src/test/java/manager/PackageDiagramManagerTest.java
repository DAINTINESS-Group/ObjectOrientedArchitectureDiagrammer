package manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.diagram.PackageDiagram;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.ast.ASTInterpreter;
import utils.ListUtils;
import utils.PathTemplate.LatexEditor;

public class PackageDiagramManagerTest {

    @Test
    void createSourceProjectTest() {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        PackageDiagram packageDiagram = packageDiagramManager.getPackageDiagram();

        Project project = new Project(LatexEditor.SRC.path);
        project.initialize();
        Collection<PackageVertex> packageNodes = project.createPackageGraph(packageDiagram);

        ASTInterpreter interpreter = new ASTInterpreter();
        interpreter.parseProject(LatexEditor.SRC.path);
        interpreter.convertToGraph(Collections.emptyList(), Collections.emptyList());

        ArrayList<PackageVertex> interpreterVertices = new ArrayList<>(interpreter.getVertices());
        assertEquals(packageNodes.size(), interpreterVertices.size());

        for (PackageVertex vertexEntry : packageNodes) {
            PackageVertex optionalPackageVertex =
                    interpreterVertices.stream()
                            .filter(
                                    it ->
                                            it.getName().equals(vertexEntry.getName())
                                                    && it.getParentVertex()
                                                            .getName()
                                                            .equals(
                                                                    vertexEntry
                                                                            .getParentVertex()
                                                                            .getName()))
                            .findFirst()
                            .orElseGet(Assertions::fail);

            assertEquals(
                    vertexEntry.getNeighborVertices().size(),
                    optionalPackageVertex.getNeighborVertices().size());

            for (PackageVertex neighbourPackageVertex : vertexEntry.getNeighborVertices()) {
                assertTrue(
                        optionalPackageVertex.getNeighborVertices().stream()
                                .anyMatch(
                                        it ->
                                                it.getName()
                                                        .equals(neighbourPackageVertex.getName())));
            }

            assertEquals(
                    vertexEntry.getSinkVertices().size(),
                    optionalPackageVertex.getSinkVertices().size());

            for (ClassifierVertex classifierVertex : vertexEntry.getSinkVertices()) {
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

        Project project = new Project(LatexEditor.SRC.path);
        project.initialize();
        Collection<PackageVertex> packageNodes = project.createPackageGraph(packageDiagram);

        packageDiagramManager.convertTreeToDiagram(
                List.of(
                        "src.view",
                        "src.model",
                        "src.model.strategies",
                        "src.controller.commands",
                        "src.controller"));

        Map<PackageVertex, Integer> graphNodes =
                packageDiagramManager.getPackageDiagram().getGraphNodes();
        packageNodes =
                packageNodes.stream()
                        .filter(it -> it.getPath() != LatexEditor.SRC.path)
                        .collect(Collectors.toSet());

        assertEquals(packageNodes.size(), graphNodes.size());

        List<String> l1 =
                packageNodes.stream()
                        .map(PackageVertex::getName)
                        .collect(Collectors.toCollection(ArrayList::new));

        List<String> l2 =
                graphNodes.keySet().stream()
                        .map(PackageVertex::getName)
                        .collect(Collectors.toCollection(ArrayList::new));

        ListUtils.assertListsEqual(l1, l2);
    }
}
