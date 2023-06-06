package manager;

import jdk.jshell.Diag;
import model.diagram.DiagramExporter;
import model.diagram.arrangement.ClassDiagramArrangement;
import model.diagram.GraphClassDiagramConverter;
import model.diagram.arrangement.DiagramArrangement;
import model.diagram.graphml.GraphMLClassDiagramExporter;
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
            GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
            Map<SinkVertex, Set<Arc<SinkVertex>>> adjacencyList = graphClassDiagramConverter.convertGraphToClassDiagram();

            assertEquals(adjacencyList, testingCreatedDiagram);
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
            DiagramArrangement classDiagramArrangement = new ClassDiagramArrangement(graphNodes, graphEdges);
            Map<Integer, List<Double>> nodesGeometry = classDiagramArrangement.arrangeDiagram();
            GraphMLSinkVertex graphMLSinkVertex = new GraphMLSinkVertex(graphNodes, nodesGeometry);
            StringBuilder graphMLNodeBuffer = graphMLSinkVertex.convertLeafNode();
            GraphMLSinkVertexArc graphMLSinkVertexArc = new GraphMLSinkVertexArc(graphNodes);
            StringBuilder graphMLEdgeBuffer = graphMLSinkVertexArc.convertLeafEdge(graphEdges);

            DiagramExporter graphMLExporter = new GraphMLClassDiagramExporter(graphNodes, nodesGeometry, graphEdges);
            assertTrue(FileUtils.contentEquals(graphMLExporter.exportDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML")
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

            File testingSavedFile = classDiagramManager.saveDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.txt"));
            DiagramExporter javaFXExporter = new JavaFXClassDiagramExporter(createdDiagram);
            assertTrue(FileUtils.contentEquals(javaFXExporter.exportDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.txt")), testingSavedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
            classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.createDiagram(chosenFiles);
            Map<SinkVertex, Set<Arc<SinkVertex>>> createdDiagram = classDiagramManager.getCreatedDiagram();
            classDiagramManager.saveDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.txt"));
            classDiagramManager.loadDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.txt"));
            Map<SinkVertex, Set<Arc<SinkVertex>>> loadedDiagram = classDiagramManager.getCreatedDiagram();

            for (SinkVertex sinkVertex: createdDiagram.keySet()) {
                Optional<SinkVertex> optionalSinkVertex = loadedDiagram.keySet().stream().filter(sinkVertex1 ->
                    sinkVertex1.getName().equals(sinkVertex.getName())
                ).findFirst();
                assertTrue(optionalSinkVertex.isPresent());

                assertEquals(createdDiagram.get(sinkVertex).size(), loadedDiagram.get(optionalSinkVertex.get()).size());
                for (Arc<SinkVertex> arc: createdDiagram.get(sinkVertex)) {
                    loadedDiagram.get(optionalSinkVertex.get()).stream().filter(a ->
                        a.getSourceVertex().getName().equals(arc.getSourceVertex().getName()) &&
                        a.getTargetVertex().getName().equals(arc.getTargetVertex().getName()) &&
                        a.getArcType().equals(arc.getArcType()))
                    .findFirst().orElseGet(Assertions::fail);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
