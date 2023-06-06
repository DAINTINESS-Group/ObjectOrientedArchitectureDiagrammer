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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphPackageDiagramConverterTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertGraphToPackageDiagramTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.createDiagram(List.of(
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\view",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller"));
            Set<Vertex> graphNodes = packageDiagramManager.getDiagram().getGraphNodes().keySet();
            Set<Arc<Vertex>> graphEdges = packageDiagramManager.getDiagram().getGraphEdges().keySet();

            GraphPackageDiagramConverter graphPackageDiagramConverter = new GraphPackageDiagramConverter(graphNodes);
            Map<Vertex, Set<Arc<Vertex>>> adjacencyList = graphPackageDiagramConverter.convertGraphToPackageDiagram();

            Set<Arc<Vertex>> actualArcs = new HashSet<>();
            for (Set<Arc<Vertex>> value : adjacencyList.values()) {
                actualArcs.addAll(value);
            }

            assertEquals(graphEdges.size(), actualArcs.size());
            for (Arc<Vertex> vertexArc: actualArcs) {
                assertTrue(graphEdges.contains(vertexArc));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
