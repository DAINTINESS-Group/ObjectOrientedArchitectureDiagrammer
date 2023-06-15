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
            classDiagramManager.getClassDiagram().createDiagram(chosenFiles);
            Set<SinkVertex> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes().keySet();
            Set<Arc<SinkVertex>> graphEdges = classDiagramManager.getClassDiagram().getGraphEdges().keySet();

            GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes);
            Map<SinkVertex, Set<Arc<SinkVertex>>> adjacencyList = graphClassDiagramConverter.convertGraphToClassDiagram();

            Set<Arc<SinkVertex>> actualArcs = new HashSet<>();
            for (Set<Arc<SinkVertex>> value : adjacencyList.values()) {
                actualArcs.addAll(value);
            }

            assertEquals(graphEdges.size(), actualArcs.size());
            for (Arc<SinkVertex> arc: actualArcs) {
                assertTrue(graphEdges.contains(arc));
            }

            assertEquals(graphNodes.size(), adjacencyList.keySet().size());
            for (SinkVertex sinkVertex: adjacencyList.keySet()) {
                assertTrue(graphNodes.contains(sinkVertex));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
