package manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import model.diagram.ClassDiagram;
import model.diagram.GraphClassDiagramConverter;
import model.diagram.ShadowCleaner;
import model.diagram.arrangement.ClassDiagramArrangementManager;
import model.diagram.arrangement.DiagramArrangementManager;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.GraphMLClassDiagramExporter;
import model.diagram.exportation.JavaFXClassDiagramExporter;
import model.diagram.graphml.GraphMLClassifierVertex;
import model.diagram.graphml.GraphMLClassifierVertexArc;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.ast.ASTInterpreter;
import utils.PathConstructor;
import utils.PathTemplate.LatexEditor;

public class ClassDiagramManagerTest {

    @Test
    void createSourceProjectTest() {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        ClassDiagram classDiagram = classDiagramManager.getClassDiagram();

        Project project = new Project(LatexEditor.SRC.path);
        Collection<ClassifierVertex> vertices = project.createClassGraph(classDiagram);

        ASTInterpreter interpreter = new ASTInterpreter();
        interpreter.parseProject(LatexEditor.SRC.path);
        interpreter.convertToGraph();

        Collection<ClassifierVertex> sinkVertices = interpreter.getSinkVertices();
        ArrayList<ClassifierVertex> interpreterVertices = new ArrayList<>(sinkVertices);
        assertEquals(vertices.size(), interpreterVertices.size());

        for (ClassifierVertex classifierVertex : sinkVertices) {
            ClassifierVertex classifierVertexActual =
                    vertices.stream()
                            .filter(
                                    it ->
                                            it.getName().equals(classifierVertex.getName())
                                                    && it.getPath()
                                                            .equals(classifierVertex.getPath()))
                            .findFirst()
                            .orElseGet(Assertions::fail);

            assertEquals(
                    classifierVertexActual.getFields().size(), classifierVertex.getFields().size());
            assertEquals(
                    classifierVertexActual.getMethods().size(),
                    classifierVertex.getMethods().size());
            assertEquals(classifierVertexActual.getVertexType(), classifierVertex.getVertexType());
            assertEquals(
                    classifierVertexActual.getArcs().size(), classifierVertex.getArcs().size());
        }
    }

    @Test
    void populateGraphNodesTest() {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        ClassDiagram classDiagram = classDiagramManager.getClassDiagram();

        Project project = new Project(LatexEditor.SRC.path);
        Collection<ClassifierVertex> sinkVertices = project.createClassGraph(classDiagram);

        Set<String> chosenFilesNames =
                Set.of(
                        "AddLatexCommand",
                        "ChangeVersionsStrategyCommand",
                        "Command",
                        "CommandFactory",
                        "CreateCommand",
                        "DisableVersionsManagementCommand",
                        "EditCommand",
                        "EnableVersionsManagementCommand",
                        "LoadCommand",
                        "RollbackToPreviousVersionCommand",
                        "SaveCommand");

        Map<ClassifierVertex, Integer> graphNodes =
                classDiagramManager.getClassDiagram().getGraphNodes();
        classDiagramManager.convertTreeToDiagram(new ArrayList<>(chosenFilesNames));

        List<String> l1 =
                sinkVertices.stream()
                        .map(ClassifierVertex::getName)
                        .filter(chosenFilesNames::contains)
                        .collect(Collectors.toCollection(ArrayList::new));

        List<String> l2 =
                graphNodes.keySet().stream()
                        .map(ClassifierVertex::getName)
                        .collect(Collectors.toCollection(ArrayList::new));

        Collections.sort(l1);
        Collections.sort(l2);

        assertEquals(l1.size(), l2.size());
        assertTrue(l1.containsAll(l2));
        assertTrue(l2.containsAll(l1));
    }

    @Test
    void createDiagramTest() {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = List.of("MainWindow", "LatexEditorView", "OpeningWindow");

        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(chosenFiles);
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> testingCreatedDiagram =
                classDiagramManager.getClassDiagram().getDiagram();

        Map<ClassifierVertex, Integer> graphNodes =
                classDiagramManager.getClassDiagram().getGraphNodes();
        GraphClassDiagramConverter graphClassDiagramConverter =
                new GraphClassDiagramConverter(graphNodes.keySet());
        classDiagramManager
                .getClassDiagram()
                .setDiagram(graphClassDiagramConverter.convertGraphToClassDiagram());

        ShadowCleaner shadowCleaner = new ShadowCleaner(classDiagramManager.getClassDiagram());
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> adjacencyList =
                shadowCleaner.shadowWeakRelationships();

        assertEquals(adjacencyList, testingCreatedDiagram);
    }

    @Test
    void exportDiagramToGraphMLTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = List.of("MainWindow", "LatexEditorView", "OpeningWindow");

            classDiagramManager.createSourceProject(LatexEditor.SRC.path);
            classDiagramManager.convertTreeToDiagram(chosenFiles);
            classDiagramManager.arrangeDiagram();
            File actualFile =
                    classDiagramManager.exportDiagramToGraphML(
                            Paths.get(
                                    String.format(
                                            "%s%s%s",
                                            PathConstructor.getCurrentPath(),
                                            File.separator,
                                            PathConstructor.constructPath(
                                                    "src",
                                                    "test",
                                                    "resources",
                                                    "testingExportedFile.graphML"))));

            DiagramArrangementManager classDiagramArrangement =
                    new ClassDiagramArrangementManager(classDiagramManager.getClassDiagram());
            classDiagramManager
                    .getClassDiagram()
                    .setGraphMLDiagramGeometry(classDiagramArrangement.arrangeGraphMLDiagram());
            GraphMLClassifierVertex graphMLClassifierVertex = new GraphMLClassifierVertex();
            graphMLClassifierVertex.convertSinkVertex(classDiagramManager.getClassDiagram());
            GraphMLClassifierVertexArc graphMLClassifierVertexArc =
                    new GraphMLClassifierVertexArc();
            graphMLClassifierVertexArc.convertSinkVertexArc(classDiagramManager.getClassDiagram());

            DiagramExporter graphMLExporter =
                    new GraphMLClassDiagramExporter(classDiagramManager.getClassDiagram());
            File expectedFile =
                    graphMLExporter.exportDiagram(
                            Paths.get(
                                    String.format(
                                            "%s%s%s",
                                            PathConstructor.getCurrentPath(),
                                            File.separator,
                                            PathConstructor.constructPath(
                                                    "src",
                                                    "test",
                                                    "resources",
                                                    "testingExportedFile.graphML"))));
            assertTrue(FileUtils.contentEquals(expectedFile, actualFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = List.of("MainWindow", "LatexEditorView", "OpeningWindow");
            classDiagramManager.createSourceProject(LatexEditor.SRC.path);
            classDiagramManager.convertTreeToDiagram(chosenFiles);

            File testingSavedFile =
                    classDiagramManager.saveDiagram(
                            Paths.get(
                                    String.format(
                                            "%s%s%s",
                                            PathConstructor.getCurrentPath(),
                                            File.separator,
                                            PathConstructor.constructPath(
                                                    "src",
                                                    "test",
                                                    "resources",
                                                    "testingSavedFile.txt"))));
            DiagramExporter javaFXExporter =
                    new JavaFXClassDiagramExporter(classDiagramManager.getClassDiagram());
            assertTrue(
                    FileUtils.contentEquals(
                            javaFXExporter.exportDiagram(
                                    Paths.get(
                                            String.format(
                                                    "%s%s%s",
                                                    PathConstructor.getCurrentPath(),
                                                    File.separator,
                                                    PathConstructor.constructPath(
                                                            "src",
                                                            "test",
                                                            "resources",
                                                            "testingExportedFile.txt")))),
                            testingSavedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadDiagramTest() {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = List.of("MainWindow", "LatexEditorView", "OpeningWindow");
        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(chosenFiles);
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> createdDiagram =
                classDiagramManager.getClassDiagram().getDiagram();
        classDiagramManager.saveDiagram(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src", "test", "resources", "testingExportedFile.txt"))));
        classDiagramManager.loadDiagram(
                Paths.get(
                        String.format(
                                "%s%s%s",
                                PathConstructor.getCurrentPath(),
                                File.separator,
                                PathConstructor.constructPath(
                                        "src", "test", "resources", "testingExportedFile.txt"))));
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> loadedDiagram =
                classDiagramManager.getClassDiagram().getDiagram();

        for (ClassifierVertex classifierVertex : createdDiagram.keySet()) {
            Optional<ClassifierVertex> optionalSinkVertex =
                    loadedDiagram.keySet().stream()
                            .filter(it -> it.getName().equals(classifierVertex.getName()))
                            .findFirst();
            assertTrue(optionalSinkVertex.isPresent());

            assertEquals(
                    createdDiagram.get(classifierVertex).size(),
                    loadedDiagram.get(optionalSinkVertex.get()).size());
            for (Arc<ClassifierVertex> arc : createdDiagram.get(classifierVertex)) {
                assertTrue(
                        loadedDiagram.get(optionalSinkVertex.get()).stream()
                                .anyMatch(
                                        it ->
                                                it.sourceVertex()
                                                                .getName()
                                                                .equals(
                                                                        arc.sourceVertex()
                                                                                .getName())
                                                        && it.targetVertex()
                                                                .getName()
                                                                .equals(
                                                                        arc.targetVertex()
                                                                                .getName())
                                                        && it.arcType().equals(arc.arcType())));
            }
        }
    }
}
