package gr.uoi.ooad.model.javafx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import gr.uoi.ooad.manager.PackageDiagramManager;
import gr.uoi.ooad.model.diagram.exportation.DiagramExporter;
import gr.uoi.ooad.model.diagram.exportation.JavaFXPackageDiagramExporter;
import gr.uoi.ooad.model.diagram.javafx.JavaFXPackageDiagramLoader;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import gr.uoi.ooad.utils.PathConstructor;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;

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

            List<Arc<PackageVertex>> arcs = optionalVertex.get().getArcs();
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

            List<ClassifierVertex> sinkVertices = optionalVertex.get().getSinkVertices();
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

            List<PackageVertex> neighbours = optionalVertex.get().getNeighbourVertices();
            assertEquals(vertex.getNeighbourVertices().size(), neighbours.size());
            for (PackageVertex neighbour : vertex.getNeighbourVertices()) {
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
