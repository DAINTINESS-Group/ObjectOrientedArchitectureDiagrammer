package model;

import manager.ClassDiagramManager;
import manager.DiagramManager;
import model.diagram.CollectionsDiagramConverter;
import model.diagram.GraphEdgeCollection;
import model.diagram.GraphNodeCollection;
import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.tree.node.Node;
import model.tree.node.PackageNode;
import model.tree.edge.Relationship;
import model.tree.SourceProject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionsDiagramConvertTest {

    Path currentDirectory = Path.of(".");
    @Test
    void convertCollectionsToDiagramTest() throws IOException {
        DiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
        SourceProject sourceProject = classDiagramManager.createTree(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));

        GraphNodeCollection graphNodeCollection = new GraphMLLeafNode();
        GraphEdgeCollection graphEdgeCollection = new GraphMLLeafEdge();
        graphNodeCollection.populateGraphNodes(getChosenNodes(chosenFiles, sourceProject));
        graphEdgeCollection.setGraphNodes(graphNodeCollection.getGraphNodes());
        graphEdgeCollection.populateGraphEdges(getChosenNodes(chosenFiles, sourceProject));

        CollectionsDiagramConverter collectionsDiagramConverter = new CollectionsDiagramConverter(graphNodeCollection, graphEdgeCollection);
        Map<String, Map<String, String>> actualDiagram = collectionsDiagramConverter.convertCollectionsToDiagram();

        Map<String, Map<String, String>> expectedDiagram = new HashMap<>();
        for (Node leafNode: graphNodeCollection.getGraphNodes().keySet()) {
            Map<String, String> nodeEdges = new HashMap<>();
            for (Relationship relationship: graphEdgeCollection.getGraphEdges().keySet()){
                if (relationship.getStartingNode().equals(leafNode)) {
                    nodeEdges.put(relationship.getEndingNode().getName() + "_" + relationship.getEndingNode().getType().name() +
                            "_" + relationship.getRelationshipType(), relationship.getRelationshipType().name());
                }
            }
            expectedDiagram.put(leafNode.getName() + "_" + leafNode.getType().name(), nodeEdges);
        }

        for (Map.Entry<String, Map<String, String>> entry: expectedDiagram.entrySet()) {
            assertTrue(actualDiagram.containsKey(entry.getKey()));
            for (Map.Entry<String, String> entry1: entry.getValue().entrySet()) {
                assertTrue(actualDiagram.get(entry.getKey()).containsKey(entry1.getKey()));
                assertEquals(entry1.getValue(), actualDiagram.get(entry.getKey()).get(entry1.getKey()));
            }
        }
    }

    private List<Node> getChosenNodes(List<String> chosenClassesNames, SourceProject sourceProject) {
        List<Node> chosenClasses = new ArrayList<>();
        for (String chosenClass: chosenClassesNames) {
            for (PackageNode p: sourceProject.getPackageNodes().values()){
                if (p.getLeafNodes().containsKey(chosenClass)) {
                    chosenClasses.add(p.getLeafNodes().get(chosenClass));
                    break;
                }
            }
        }
        return chosenClasses;
    }
}
