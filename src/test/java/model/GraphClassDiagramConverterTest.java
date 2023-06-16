package model;

import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.GraphClassDiagramConverter;
import model.diagram.ShadowCleaner;
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
            classDiagramManager.convertTreeToDiagram(chosenFiles);
            Set<SinkVertex> graphNodes = classDiagramManager.getGraphNodes().keySet();
            Map<SinkVertex, Set<Arc<SinkVertex>>> diagram = classDiagramManager.getDiagram();

            List<Arc<SinkVertex>> arcs = new ArrayList<>();
            for (Set<Arc<SinkVertex>> arcSet: diagram.values()) {
                arcs.addAll(arcSet);
            }

            GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(diagram.keySet());
            Map<SinkVertex, Set<Arc<SinkVertex>>> adjacencyList = graphClassDiagramConverter.convertGraphToClassDiagram();
            ShadowCleaner shadowCleaner = new ShadowCleaner(adjacencyList);
            adjacencyList = shadowCleaner.shadowWeakRelationships();

            Set<Arc<SinkVertex>> actualArcs = new HashSet<>();
            for (Set<Arc<SinkVertex>> value : adjacencyList.values()) {
                actualArcs.addAll(value);
            }

            assertEquals(arcs.size(), actualArcs.size());
            for (Arc<SinkVertex> arc: actualArcs) {
                assertTrue(arcs.contains(arc));
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
