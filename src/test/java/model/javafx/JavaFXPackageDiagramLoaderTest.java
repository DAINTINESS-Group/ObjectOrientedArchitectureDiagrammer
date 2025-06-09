package model.javafx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import manager.PackageDiagramManager;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.JavaFXPackageDiagramExporter;
import model.diagram.javafx.JavaFXPackageDiagramLoader;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;
import utils.PathTemplate.LatexEditor;

public class JavaFXPackageDiagramLoaderTest {

    @Test
    void loadDiagramTest() {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(LatexEditor.SRC.path);
        packageDiagramManager.convertTreeToDiagram(
                List.of(
                        "src.view",
                        "src.model",
                        "src.model.strategies",
                        "src.controller.commands",
                        "src.controller"));
        Map<PackageVertex, Set<Arc<PackageVertex>>> createdDiagram =
                packageDiagramManager.getPackageDiagram().getDiagram();

        DiagramExporter javaFXExporter =
                new JavaFXPackageDiagramExporter(packageDiagramManager.getPackageDiagram());
        File actualFile =
                javaFXExporter.exportDiagram(
                        Path.of(
                                String.format(
                                        "%s%s%s",
                                        PathConstructor.getCurrentPath(),
                                        File.separator,
                                        PathConstructor.constructPath(
                                                "src",
                                                "test",
                                                "resources",
                                                "testingExportedFile.txt"))));

        Set<PackageVertex> loadedDiagram =
                JavaFXPackageDiagramLoader.loadDiagram(actualFile.toPath());
        assertEquals(createdDiagram.size(), loadedDiagram.size());

        for (PackageVertex vertex : createdDiagram.keySet()) {
            Optional<PackageVertex> optionalVertex =
                    loadedDiagram.stream()
                            .filter(
                                    it ->
                                            it.getName().equals(vertex.getName())
                                                    && it.getVertexType()
                                                            .equals(vertex.getVertexType()))
                            .findFirst();
            assertTrue(optionalVertex.isPresent());

            Set<Arc<PackageVertex>> arcs = optionalVertex.get().getArcs();
            assertEquals(createdDiagram.get(vertex).size(), arcs.size());
            for (Arc<PackageVertex> arc : createdDiagram.get(vertex)) {
                arcs.stream()
                        .filter(
                                it ->
                                        it.sourceVertex()
                                                        .getName()
                                                        .equals(arc.sourceVertex().getName())
                                                && it.targetVertex()
                                                        .getName()
                                                        .equals(arc.targetVertex().getName())
                                                && it.arcType().equals(arc.arcType()))
                        .findFirst()
                        .orElseGet(Assertions::fail);
            }

            Set<ClassifierVertex> sinkVertices = optionalVertex.get().getSinkVertices();
            assertEquals(vertex.getSinkVertices().size(), sinkVertices.size());
            for (ClassifierVertex classifierVertex : vertex.getSinkVertices()) {
                sinkVertices.stream()
                        .filter(
                                it ->
                                        it.getName().equals(classifierVertex.getName())
                                                && it.getVertexType()
                                                        .equals(classifierVertex.getVertexType()))
                        .findFirst()
                        .orElseGet(Assertions::fail);
            }

            Set<PackageVertex> neighbours = optionalVertex.get().getNeighborVertices();
            assertEquals(vertex.getNeighborVertices().size(), neighbours.size());
            for (PackageVertex neighbour : vertex.getNeighborVertices()) {
                neighbours.stream()
                        .filter(
                                it ->
                                        it.getName().equals(neighbour.getName())
                                                && it.getVertexType()
                                                        .equals(neighbour.getVertexType()))
                        .findFirst()
                        .orElseGet(Assertions::fail);
            }
        }
    }
}
