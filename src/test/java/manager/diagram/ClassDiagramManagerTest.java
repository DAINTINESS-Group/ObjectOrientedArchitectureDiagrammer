package manager.diagram;

import manager.ClassDiagramManager;
import model.SourceProject;
import model.diagram.CollectionsDiagramConverter;
import model.diagram.DiagramArrangement;
import model.diagram.graphml.GraphMLExporter;
import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.diagram.javafx.JavaFXExporter;
import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.Interpreter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassDiagramManagerTest {

    Path currentDirectory = Path.of(".");

    @Test
    void createTreeTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            Map<Path, Vertex> vertices = sourceProject.getVertices();
            Interpreter interpreter = new Interpreter();
            interpreter.parseProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            ArrayList<Vertex> interpreterVertices = interpreter.convertTreeToGraph().getKey();

            assertEquals(vertices.size(), interpreterVertices.size());
            for (Map.Entry<Path, Vertex> vertexEntry: vertices.entrySet()) {
                Vertex optionalVertex = interpreterVertices.stream()
                    .filter(vertex -> vertex.getName().equals(
                    vertexEntry.getValue().getName()) &&
                    vertex.getParentVertex().getName().equals(vertexEntry.getValue().getParentVertex().getName()))
                    .findFirst().orElseGet(Assertions::fail);

                assertEquals(vertexEntry.getValue().getNeighbourVertices().size(), optionalVertex.getNeighbourVertices().size());
                for (Vertex neighbourVertex: vertexEntry.getValue().getNeighbourVertices()) {
                    Optional<Vertex> optionalNeighbourVertex = optionalVertex.getNeighbourVertices().stream()
                        .filter(neighbour -> neighbour.getName().equals(neighbourVertex.getName()))
                        .findAny();
                    assertTrue(optionalNeighbourVertex.isPresent());
                }

                assertEquals(vertexEntry.getValue().getSinkVertices().size(), optionalVertex.getSinkVertices().size());
                for (SinkVertex sinkVertex: vertexEntry.getValue().getSinkVertices()) {
                    Optional<SinkVertex> optionalSinkVertex = optionalVertex.getSinkVertices().stream()
                        .filter(sinkVertex1 -> sinkVertex1.getName().equals(sinkVertex.getName()))
                        .findAny();
                    assertTrue(optionalSinkVertex.isPresent());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            Map<String, Map<String, String>> testingCreatedDiagram = classDiagramManager.createDiagram(chosenFiles);

            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();
            CollectionsDiagramConverter collectionsDiagramConverter = new CollectionsDiagramConverter();
            Map<String, Map<String, String>> createdDiagram = collectionsDiagramConverter.convertClassCollectionsToDiagram(graphNodes, graphEdges);

            for (Map.Entry<String, Map<String, String>> entry: createdDiagram.entrySet()) {
                assertTrue(testingCreatedDiagram.containsKey(entry.getKey()));
                for (Map.Entry<String, String> entry1: entry.getValue().entrySet()) {
                    assertTrue(testingCreatedDiagram.get(entry.getKey()).containsKey(entry1.getKey()));
                    assertEquals(entry1.getValue(), testingCreatedDiagram.get(entry.getKey()).get(entry1.getKey()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void exportDiagramToGraphMLTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.createDiagram(chosenFiles);
            classDiagramManager.arrangeDiagram();
            File testingExportedFile = classDiagramManager.exportDiagramToGraphML(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));

            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();
            DiagramArrangement diagramArrangement = new DiagramArrangement();
            Map<Integer, List<Double>> nodesGeometry = diagramArrangement.arrangeClassDiagram(graphNodes, graphEdges);
            GraphMLLeafNode graphMLLeafNode = new GraphMLLeafNode(graphNodes, nodesGeometry);
            StringBuilder graphMLNodeBuffer = graphMLLeafNode.convertLeafNode();
            GraphMLLeafEdge graphMLLeafEdge = new GraphMLLeafEdge(graphNodes);
            StringBuilder graphMLEdgeBuffer = graphMLLeafEdge.convertLeafEdge(graphEdges);

            GraphMLExporter graphMLExporter = new GraphMLExporter();
            assertTrue(FileUtils.contentEquals(graphMLExporter.exportDiagramToGraphML(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"),
                    graphMLNodeBuffer, graphMLEdgeBuffer), testingExportedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
            classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            Map<String, Map<String, String>> createdDiagram = classDiagramManager.createDiagram(chosenFiles);

            File testingSavedFile = classDiagramManager.saveDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));
            JavaFXExporter javaFXExporter = new JavaFXExporter();
            assertTrue(FileUtils.contentEquals(javaFXExporter.saveDiagram(createdDiagram,
                    Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML")), testingSavedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadDiagramTest() throws IOException {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
        classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        Map<String, Map<String, String>> createdDiagram = classDiagramManager.createDiagram(chosenFiles);
        classDiagramManager.saveDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));
        Map<String, Map<String, String>> testingLoadedDiagram = classDiagramManager.loadDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));

        for (Map.Entry<String, Map<String, String>> entry: createdDiagram.entrySet()) {
            assertTrue(testingLoadedDiagram.containsKey(entry.getKey()));
            for (Map.Entry<String, String> entry1: entry.getValue().entrySet()) {
                assertTrue(testingLoadedDiagram.get(entry.getKey()).containsKey(entry1.getKey()));
                assertEquals(entry1.getValue(), testingLoadedDiagram.get(entry.getKey()).get(entry1.getKey()));
            }
        }
    }

}
