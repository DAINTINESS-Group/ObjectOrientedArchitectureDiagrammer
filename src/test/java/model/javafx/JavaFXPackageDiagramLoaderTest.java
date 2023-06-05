package model.javafx;

import manager.PackageDiagramManager;
import model.diagram.javafx.packagediagram.JavaFXPackageDiagramExporter;
import model.diagram.javafx.packagediagram.JavaFXPackageDiagramLoader;
import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;
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
            packageDiagramManager.createDiagram(List.of(
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\view",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller"));
            Map<Vertex, Set<Arc<Vertex>>> createdDiagram = packageDiagramManager.getCreatedDiagram();

            JavaFXPackageDiagramExporter javaFXExporter = new JavaFXPackageDiagramExporter(
                    Path.of(System.getProperty("user.home") + "\\testingExportedFile.txt"), createdDiagram);
            File actualFile = javaFXExporter.saveDiagram();

            JavaFXPackageDiagramLoader javaFXLoader = new JavaFXPackageDiagramLoader(actualFile.toPath());
            Set<Vertex> loadedDiagram = javaFXLoader.loadDiagram();

            assertEquals(createdDiagram.size(), loadedDiagram.size());
            for (Vertex vertex: createdDiagram.keySet()) {
                Optional<Vertex> optionalVertex = loadedDiagram.stream()
                    .filter(vertex1 ->
                        vertex1.getName().equals(vertex.getName()) &&
                        vertex1.getVertexType().equals(vertex.getVertexType()) &&
                        vertex1.getPath().equals(vertex.getPath())
                    ).findFirst();
                assertTrue(optionalVertex.isPresent());

                List<Arc<Vertex>> arcs = optionalVertex.get().getArcs();
                assertEquals(createdDiagram.get(vertex).size(), arcs.size());
                for (Arc<Vertex> arc: createdDiagram.get(vertex)) {
                    arcs.stream().filter(vertexArc ->
                        vertexArc.getSourceVertex().getName().equals(arc.getSourceVertex().getName()) &&
                        vertexArc.getTargetVertex().getName().equals(arc.getTargetVertex().getName()) &&
                        vertexArc.getArcType().equals(arc.getArcType()))
                    .findFirst().orElseGet(Assertions::fail);
                }

                List<SinkVertex> sinkVertices = optionalVertex.get().getSinkVertices();
                assertEquals(vertex.getSinkVertices().size(), sinkVertices.size());
                for (SinkVertex sinkVertex: vertex.getSinkVertices()) {
                    sinkVertices.stream().filter(sinkVertex1 ->
                        sinkVertex1.getName().equals(sinkVertex.getName()) &&
                        sinkVertex1.getVertexType().equals(sinkVertex.getVertexType()) &&
                        sinkVertex1.getPath().equals(sinkVertex.getPath())
                    ).findFirst().orElseGet(Assertions::fail);
                }

                List<Vertex> neighbours = optionalVertex.get().getNeighbourVertices();
                assertEquals(vertex.getNeighbourVertices().size(), neighbours.size());
                for (Vertex neighbour: vertex.getNeighbourVertices()) {
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
