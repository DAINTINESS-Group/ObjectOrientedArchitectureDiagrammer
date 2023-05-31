package model;

import manager.ClassDiagramManager;
import model.diagram.CollectionsDiagramConverter;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionsDiagramConvertTest {

    Path currentDirectory = Path.of(".");
    @Test
    void convertCollectionsToDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.getDiagram().createDiagram(chosenFiles);

            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();

            CollectionsDiagramConverter collectionsDiagramConverter = new CollectionsDiagramConverter();
            Map<String, Map<String, String>> actualDiagram = collectionsDiagramConverter.convertClassCollectionsToDiagram(graphNodes, graphEdges);

            Map<String, Map<String, String>> expectedDiagram = new HashMap<>();
            for (SinkVertex leafNode: graphNodes.keySet()) {
                Map<String, String> nodeEdges = new HashMap<>();
                for (Arc<SinkVertex> relationship: graphEdges.keySet()){
                    if (relationship.getSourceVertex().equals(leafNode)) {
                        nodeEdges.put(relationship.getTargetVertex().getName() + "_" + relationship.getTargetVertex().getVertexType().name() +
                                "_" + relationship.getArcType(), relationship.getArcType().name());
                    }
                }
                expectedDiagram.put(leafNode.getName() + "_" + leafNode.getVertexType().name(), nodeEdges);
            }

            for (Map.Entry<String, Map<String, String>> entry: expectedDiagram.entrySet()) {
                assertTrue(actualDiagram.containsKey(entry.getKey()));
                for (Map.Entry<String, String> entry1: entry.getValue().entrySet()) {
                    assertTrue(actualDiagram.get(entry.getKey()).containsKey(entry1.getKey()));
                    assertEquals(entry1.getValue(), actualDiagram.get(entry.getKey()).get(entry1.getKey()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
