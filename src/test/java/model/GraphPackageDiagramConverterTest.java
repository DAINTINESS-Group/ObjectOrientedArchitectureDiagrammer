package model;

import manager.PackageDiagramManager;
import manager.SourceProject;
import model.diagram.GraphPackageDiagramConverter;
import model.graph.Arc;
import model.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphPackageDiagramConverterTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertGraphToPackageDiagramTest() {
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
            Map<Vertex, Set<Arc<Vertex>>> diagram = packageDiagramManager.getPackageDiagram().getDiagram();

            List<Arc<Vertex>> arcs = new ArrayList<>();
            for (Set<Arc<Vertex>> arcSet: diagram.values()) {
                arcs.addAll(arcSet);
            }

            GraphPackageDiagramConverter graphPackageDiagramConverter = new GraphPackageDiagramConverter(diagram.keySet());
            Map<Vertex, Set<Arc<Vertex>>> adjacencyList = graphPackageDiagramConverter.convertGraphToPackageDiagram();

            Set<Arc<Vertex>> actualArcs = new HashSet<>();
            for (Set<Arc<Vertex>> value : adjacencyList.values()) {
                actualArcs.addAll(value);
            }

            assertEquals(arcs.size(), actualArcs.size());
            for (Arc<Vertex> vertexArc: actualArcs) {
                assertTrue(arcs.contains(vertexArc));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
