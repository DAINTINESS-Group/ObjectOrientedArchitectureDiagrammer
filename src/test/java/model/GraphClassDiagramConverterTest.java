package model;

import manager.ClassDiagramManager;
import model.diagram.GraphClassDiagramConverter;
import model.diagram.ShadowCleaner;
import model.graph.Arc;
import model.graph.ClassifierVertex;
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
            classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.convertTreeToDiagram(chosenFiles);
            Set<ClassifierVertex> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes().keySet();
            Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> diagram = classDiagramManager.getClassDiagram().getDiagram();

            List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
            for (Set<Arc<ClassifierVertex>> arcSet: diagram.values()) {
                arcs.addAll(arcSet);
            }

            GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(diagram.keySet());
            Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> adjacencyList = graphClassDiagramConverter.convertGraphToClassDiagram();
            classDiagramManager.getClassDiagram().setDiagram(adjacencyList);
            ShadowCleaner shadowCleaner = new ShadowCleaner(classDiagramManager.getClassDiagram());
            adjacencyList = shadowCleaner.shadowWeakRelationships();

            Set<Arc<ClassifierVertex>> actualArcs = new HashSet<>();
            for (Set<Arc<ClassifierVertex>> value : adjacencyList.values()) {
                actualArcs.addAll(value);
            }

            assertEquals(arcs.size(), actualArcs.size());
            for (Arc<ClassifierVertex> arc: actualArcs) {
                assertTrue(arcs.contains(arc));
            }

            assertEquals(graphNodes.size(), adjacencyList.keySet().size());
            for (ClassifierVertex classifierVertex : adjacencyList.keySet()) {
                assertTrue(graphNodes.contains(classifierVertex));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
