package gr.uoi.ooad.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gr.uoi.ooad.manager.ClassDiagramManager;
import gr.uoi.ooad.model.diagram.GraphClassDiagramConverter;
import gr.uoi.ooad.model.diagram.ShadowCleaner;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

public class GraphClassDiagramConverterTest {

    @Test
    void convertGraphToClassDiagramTest() {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = List.of("MainWindow", "LatexEditorView", "OpeningWindow");

        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(chosenFiles);

        Set<ClassifierVertex> graphNodes =
                classDiagramManager.getClassDiagram().getGraphNodes().keySet();
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> diagram =
                classDiagramManager.getClassDiagram().getDiagram();

        List<Arc<ClassifierVertex>> arcs =
                diagram.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(ArrayList::new));

        GraphClassDiagramConverter graphClassDiagramConverter =
                new GraphClassDiagramConverter(diagram.keySet());
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> adjacencyList =
                graphClassDiagramConverter.convertGraphToClassDiagram();
        classDiagramManager.getClassDiagram().setDiagram(adjacencyList);
        ShadowCleaner shadowCleaner = new ShadowCleaner(classDiagramManager.getClassDiagram());
        adjacencyList = shadowCleaner.shadowWeakRelationships();

        Set<Arc<ClassifierVertex>> actualArcs =
                adjacencyList.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(HashSet::new));

        assertEquals(arcs.size(), actualArcs.size());
        assertTrue(actualArcs.containsAll(arcs));

        assertEquals(graphNodes.size(), adjacencyList.keySet().size());
        assertTrue(graphNodes.containsAll(adjacencyList.keySet()));
    }
}
