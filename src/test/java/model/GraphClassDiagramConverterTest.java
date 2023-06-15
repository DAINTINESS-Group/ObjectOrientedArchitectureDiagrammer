package model;

import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.GraphClassDiagramConverter;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphClassDiagramConverterTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertGraphToClassDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.getDiagram().createDiagram(chosenFiles);
            Set<SinkVertex> graphNodes = classDiagramManager.getDiagram().getGraphNodes().keySet();
            Set<Arc<SinkVertex>> graphEdges = classDiagramManager.getDiagram().getGraphEdges().keySet();

            GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes);
            Map<SinkVertex, Set<Arc<SinkVertex>>> adjacencyList = graphClassDiagramConverter.convertGraphToClassDiagram();

            Set<Arc<SinkVertex>> actualArcs = new HashSet<>();
            for (Set<Arc<SinkVertex>> value : adjacencyList.values()) {
                actualArcs.addAll(value);
            }

            assertEquals(graphEdges.size(), actualArcs.size());
            for (Arc<SinkVertex> sinkVertex: actualArcs) {
                assertTrue(graphEdges.contains(sinkVertex));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
