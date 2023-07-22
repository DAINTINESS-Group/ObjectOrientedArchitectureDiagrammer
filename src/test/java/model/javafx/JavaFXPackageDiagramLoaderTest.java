package model.javafx;

import manager.PackageDiagramManager;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.JavaFXPackageDiagramExporter;
import model.diagram.javafx.JavaFXPackageDiagramLoader;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaFXPackageDiagramLoaderTest {

    Path currentDirectory = Path.of(".");

    @Test
    void loadDiagramTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.convertTreeToDiagram(List.of(
                "src.view",
                "src.model",
                "src.model.strategies",
                "src.controller.commands",
                "src.controller"
            ));
            Map<PackageVertex, Set<Arc<PackageVertex>>> createdDiagram = packageDiagramManager.getPackageDiagram().getDiagram();

            DiagramExporter javaFXExporter = new JavaFXPackageDiagramExporter(packageDiagramManager.getPackageDiagram());
            File actualFile = javaFXExporter.exportDiagram(Path.of(System.getProperty("user.home") + "\\testingExportedFile.txt"));

            JavaFXPackageDiagramLoader javaFXLoader = new JavaFXPackageDiagramLoader(actualFile.toPath());
            Set<PackageVertex> loadedDiagram = javaFXLoader.loadDiagram();
            assertEquals(createdDiagram.size(), loadedDiagram.size());
            for (PackageVertex vertex: createdDiagram.keySet()) {
                Optional<PackageVertex> optionalVertex = loadedDiagram.stream()
                    .filter(vertex1 ->
                        vertex1.getName().equals(vertex.getName()) &&
                        vertex1.getVertexType().equals(vertex.getVertexType()) &&
                        vertex1.getPath().equals(vertex.getPath())
                    ).findFirst();
                assertTrue(optionalVertex.isPresent());

                List<Arc<PackageVertex>> arcs = optionalVertex.get().getArcs();
                assertEquals(createdDiagram.get(vertex).size(), arcs.size());
                for (Arc<PackageVertex> arc: createdDiagram.get(vertex)) {
                    arcs.stream().filter(vertexArc ->
                        vertexArc.getSourceVertex().getName().equals(arc.getSourceVertex().getName()) &&
                        vertexArc.getTargetVertex().getName().equals(arc.getTargetVertex().getName()) &&
                        vertexArc.getArcType().equals(arc.getArcType()))
                    .findFirst().orElseGet(Assertions::fail);
                }

                List<ClassifierVertex> sinkVertices = optionalVertex.get().getSinkVertices();
                assertEquals(vertex.getSinkVertices().size(), sinkVertices.size());
                for (ClassifierVertex classifierVertex : vertex.getSinkVertices()) {
                    sinkVertices.stream().filter(sinkVertex1 ->
                        sinkVertex1.getName().equals(classifierVertex.getName()) &&
                        sinkVertex1.getVertexType().equals(classifierVertex.getVertexType()) &&
                        sinkVertex1.getPath().equals(classifierVertex.getPath())
                    ).findFirst().orElseGet(Assertions::fail);
                }

                List<PackageVertex> neighbours = optionalVertex.get().getNeighbourVertices();
                assertEquals(vertex.getNeighbourVertices().size(), neighbours.size());
                for (PackageVertex neighbour: vertex.getNeighbourVertices()) {
                    neighbours.stream().filter(vertex1 ->
                        vertex1.getName().equals(neighbour.getName()) &&
                        vertex1.getVertexType().equals(neighbour.getVertexType()) &&
                        vertex1.getPath().equals(neighbour.getPath())
                    ).findFirst().orElseGet(Assertions::fail);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
