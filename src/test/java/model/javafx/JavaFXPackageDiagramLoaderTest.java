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
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaFXPackageDiagramLoaderTest
{

    @Test
    void loadDiagramTest()
    {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(Paths.get(String.format("%s%s%s",
                                                                          PathConstructor.getCurrentPath(),
                                                                          File.separator,
                                                                          PathConstructor.constructPath("src",
                                                                                                        "test",
                                                                                                        "resources",
                                                                                                        "LatexEditor",
                                                                                                        "src"))));
        packageDiagramManager.convertTreeToDiagram(List.of("src.view",
                                                           "src.model",
                                                           "src.model.strategies",
                                                           "src.controller.commands",
                                                           "src.controller"));
        Map<PackageVertex, Set<Arc<PackageVertex>>> createdDiagram = packageDiagramManager.getPackageDiagram().getDiagram();

        DiagramExporter javaFXExporter = new JavaFXPackageDiagramExporter(packageDiagramManager.getPackageDiagram());
        File actualFile = javaFXExporter.exportDiagram(Path.of(String.format("%s%s%s",
                                                                             PathConstructor.getCurrentPath(),
                                                                             File.separator,
                                                                             PathConstructor.constructPath("src",
                                                                                                           "test",
                                                                                                           "resources",
                                                                                                           "testingExportedFile.txt"))));

        JavaFXPackageDiagramLoader javaFXLoader  = new JavaFXPackageDiagramLoader(actualFile.toPath());
        Set<PackageVertex>         loadedDiagram = javaFXLoader.loadDiagram();
        assertEquals(createdDiagram.size(), loadedDiagram.size());
        for (PackageVertex vertex : createdDiagram.keySet())
        {
            Optional<PackageVertex> optionalVertex = loadedDiagram
                .stream()
                .filter(vertex1 ->
                            vertex1.getName().equals(vertex.getName()) &&
                            vertex1.getVertexType().equals(vertex.getVertexType()))
                .findFirst();
            assertTrue(optionalVertex.isPresent());

            List<Arc<PackageVertex>> arcs = optionalVertex.get().getArcs();
            assertEquals(createdDiagram.get(vertex).size(), arcs.size());
            for (Arc<PackageVertex> arc : createdDiagram.get(vertex))
            {
                arcs
                    .stream()
                    .filter(vertexArc ->
                                vertexArc.sourceVertex().getName().equals(arc.sourceVertex().getName()) &&
                                vertexArc.targetVertex().getName().equals(arc.targetVertex().getName()) &&
                                vertexArc.arcType().equals(arc.arcType()))
                    .findFirst()
                    .orElseGet(Assertions::fail);
            }

            List<ClassifierVertex> sinkVertices = optionalVertex.get().getSinkVertices();
            assertEquals(vertex.getSinkVertices().size(), sinkVertices.size());
            for (ClassifierVertex classifierVertex : vertex.getSinkVertices())
            {
                sinkVertices
                    .stream()
                    .filter(sinkVertex1 ->
                                sinkVertex1.getName().equals(classifierVertex.getName()) &&
                                sinkVertex1.getVertexType().equals(classifierVertex.getVertexType()))
                    .findFirst()
                    .orElseGet(Assertions::fail);
            }

            List<PackageVertex> neighbours = optionalVertex.get().getNeighbourVertices();
            assertEquals(vertex.getNeighbourVertices().size(), neighbours.size());
            for (PackageVertex neighbour : vertex.getNeighbourVertices())
            {
                neighbours
                    .stream()
                    .filter(vertex1 ->
                                vertex1.getName().equals(neighbour.getName()) &&
                                vertex1.getVertexType().equals(neighbour.getVertexType()))
                    .findFirst()
                    .orElseGet(Assertions::fail);
            }
        }
    }
}
