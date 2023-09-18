package model;

import manager.PackageDiagramManager;
import model.diagram.GraphPackageDiagramConverter;
import model.graph.Arc;
import model.graph.PackageVertex;
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
            packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.convertTreeToDiagram(List.of(
                "src.view",
                "src.model",
                "src.model.strategies",
                "src.controller.commands",
                "src.controller"
            ));
            Map<PackageVertex, Set<Arc<PackageVertex>>> diagram = packageDiagramManager.getPackageDiagram().getDiagram();

            List<Arc<PackageVertex>> arcs = new ArrayList<>();
            for (Set<Arc<PackageVertex>> arcSet: diagram.values()) {
                arcs.addAll(arcSet);
            }

            GraphPackageDiagramConverter graphPackageDiagramConverter = new GraphPackageDiagramConverter(diagram.keySet());
            Map<PackageVertex, Set<Arc<PackageVertex>>> adjacencyList = graphPackageDiagramConverter.convertGraphToPackageDiagram();

            Set<Arc<PackageVertex>> actualArcs = new HashSet<>();
            for (Set<Arc<PackageVertex>> value : adjacencyList.values()) {
                actualArcs.addAll(value);
            }

            assertEquals(arcs.size(), actualArcs.size());
            for (Arc<PackageVertex> vertexArc: actualArcs) {
                assertTrue(arcs.contains(vertexArc));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
