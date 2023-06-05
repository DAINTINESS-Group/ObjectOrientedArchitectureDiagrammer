package manager;

import model.diagram.CollectionsDiagramConverter;
import model.diagram.DiagramArrangement;
import model.diagram.graphml.GraphMLClassExporter;
import model.diagram.graphml.GraphMLSinkVertex;
import model.diagram.graphml.GraphMLSinkVertexArc;
import model.diagram.javafx.classdiagram.JavaFXClassDiagramExporter;
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
    void createSourceProjectTest() {
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
            classDiagramManager.createDiagram(chosenFiles);
            Map<SinkVertex, Set<Arc<SinkVertex>>> testingCreatedDiagram = classDiagramManager.getCreatedDiagram();

            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();
            CollectionsDiagramConverter collectionsDiagramConverter = new CollectionsDiagramConverter();

            /*
            Map<String, Map<String, String>> createdDiagram = collectionsDiagramConverter.convertClassCollectionsToDiagram(graphNodes, graphEdges);

            for (Map.Entry<String, Map<String, String>> entry: createdDiagram.entrySet()) {
                assertTrue(testingCreatedDiagram.containsKey(entry.getKey()));
                for (Map.Entry<String, String> entry1: entry.getValue().entrySet()) {
                    assertTrue(testingCreatedDiagram.get(entry.getKey()).containsKey(entry1.getKey()));
                    assertEquals(entry1.getValue(), testingCreatedDiagram.get(entry.getKey()).get(entry1.getKey()));
                }
            }

             */
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
            GraphMLSinkVertex graphMLSinkVertex = new GraphMLSinkVertex(graphNodes, nodesGeometry);
            StringBuilder graphMLNodeBuffer = graphMLSinkVertex.convertLeafNode();
            GraphMLSinkVertexArc graphMLSinkVertexArc = new GraphMLSinkVertexArc(graphNodes);
            StringBuilder graphMLEdgeBuffer = graphMLSinkVertexArc.convertLeafEdge(graphEdges);

            GraphMLClassExporter graphMLExporter = new GraphMLClassExporter(graphNodes, nodesGeometry, graphEdges);
            assertTrue(FileUtils.contentEquals(graphMLExporter.exportDiagramToGraphML(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML")
            ), testingExportedFile));
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
            classDiagramManager.createDiagram(chosenFiles);
            Map<SinkVertex, Set<Arc<SinkVertex>>> createdDiagram = classDiagramManager.getCreatedDiagram();

            File testingSavedFile = classDiagramManager.saveDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));
            JavaFXClassDiagramExporter javaFXExporter = new JavaFXClassDiagramExporter(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.txt"), createdDiagram);
            assertTrue(FileUtils.contentEquals(javaFXExporter.saveDiagram(
            ), testingSavedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadDiagramTest() throws IOException {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
        classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        classDiagramManager.createDiagram(chosenFiles);
        Map<SinkVertex, Set<Arc<SinkVertex>>> createdDiagram = classDiagramManager.getCreatedDiagram();
        classDiagramManager.saveDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));
        classDiagramManager.loadDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));

        /*
        for (Map.Entry<String, Map<String, String>> entry: createdDiagram.entrySet()) {
            assertTrue(testingLoadedDiagram.containsKey(entry.getKey()));
            for (Map.Entry<String, String> entry1: entry.getValue().entrySet()) {
                assertTrue(testingLoadedDiagram.get(entry.getKey()).containsKey(entry1.getKey()));
                assertEquals(entry1.getValue(), testingLoadedDiagram.get(entry.getKey()).get(entry1.getKey()));
            }
        }

         */
    }

}
