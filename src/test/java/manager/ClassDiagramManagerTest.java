package manager;

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
import model.graph.PackageVertex;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.Interpreter;
import utils.PathConstructor;
import utils.PathTemplate.LatexEditor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassDiagramManagerTest
{

    @Test
    void createSourceProjectTest()
    {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        SourceProject       sourceProject       = classDiagramManager.createSourceProject(LatexEditor.SRC.path);

        Map<Path, PackageVertex> vertices       = sourceProject.getInterpreter().getVertices();
        Interpreter              interpreter    = new Interpreter();
        interpreter.parseProject(LatexEditor.SRC.path);
        interpreter.convertTreeToGraph();
        ArrayList<PackageVertex> interpreterVertices = new ArrayList<>(interpreter.getVertices().values());

        assertEquals(vertices.size(), interpreterVertices.size());
        for (Map.Entry<Path, PackageVertex> vertexEntry : vertices.entrySet())
        {
            PackageVertex optionalPackageVertex = interpreterVertices.stream()
                .filter(it -> it.getName().equals(vertexEntry.getValue().getName()) &&
                              it.getParentVertex().getName().equals(vertexEntry.getValue().getParentVertex().getName()))
                .findFirst().orElseGet(Assertions::fail);

            assertEquals(vertexEntry.getValue().getNeighbourVertices().size(), optionalPackageVertex.getNeighbourVertices().size());
            for (PackageVertex neighbourPackageVertex : vertexEntry.getValue().getNeighbourVertices())
            {
                assertTrue(optionalPackageVertex.getNeighbourVertices().stream()
                    .anyMatch(it -> it.getName().equals(neighbourPackageVertex.getName())));
            }

            assertEquals(vertexEntry.getValue().getSinkVertices().size(), optionalPackageVertex.getSinkVertices().size());
            for (ClassifierVertex classifierVertex : vertexEntry.getValue().getSinkVertices())
            {
                assertTrue(optionalPackageVertex.getSinkVertices().stream()
                    .anyMatch(it -> it.getName().equals(classifierVertex.getName())));
            }
        }
    }


    @Test
    void populateGraphNodesTest()
    {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        SourceProject       sourceProject       = classDiagramManager.createSourceProject(LatexEditor.SRC.path);

        classDiagramManager.convertTreeToDiagram(List.of("AddLatexCommand",
                                                         "ChangeVersionsStrategyCommand",
                                                         "Command",
                                                         "CommandFactory",
                                                         "CreateCommand",
                                                         "DisableVersionsManagementCommand",
                                                         "EditCommand",
                                                         "EnableVersionsManagementCommand",
                                                         "LoadCommand",
                                                         "RollbackToPreviousVersionCommand",
                                                         "SaveCommand"));

        Map<ClassifierVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();
        assertEquals(sourceProject.getInterpreter().getVertices().get(LatexEditor.COMMANDS.path).getSinkVertices().size(), graphNodes.size());

        List<String> l1 = sourceProject.getInterpreter().getVertices().get(LatexEditor.COMMANDS.path).getSinkVertices().stream()
            .map(ClassifierVertex::getName)
            .collect(Collectors.toCollection(ArrayList::new));

        List<String> l2 = graphNodes.keySet().stream()
            .map(ClassifierVertex::getName)
            .collect(Collectors.toCollection(ArrayList::new));

        Collections.sort(l1);
        Collections.sort(l2);

        assertEquals(l1.size(), l2.size());
        assertTrue(l1.containsAll(l2));
        assertTrue(l2.containsAll(l1));
    }


    @Test
    void createDiagramTest()
    {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow",
                                                 "LatexEditorView",
                                                 "OpeningWindow");
        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(chosenFiles);
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> testingCreatedDiagram = classDiagramManager.getClassDiagram().getDiagram();

        Map<ClassifierVertex, Integer> graphNodes                 = classDiagramManager.getClassDiagram().getGraphNodes();
        GraphClassDiagramConverter     graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
        classDiagramManager.getClassDiagram().setDiagram(graphClassDiagramConverter.convertGraphToClassDiagram());

        ShadowCleaner                                     shadowCleaner = new ShadowCleaner(classDiagramManager.getClassDiagram());
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> adjacencyList = shadowCleaner.shadowWeakRelationships();

        assertEquals(adjacencyList, testingCreatedDiagram);
    }


    @Test
    void exportDiagramToGraphMLTest()
    {
        try
        {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow",
                                                     "LatexEditorView",
                                                     "OpeningWindow");
            classDiagramManager.createSourceProject(LatexEditor.SRC.path);
            classDiagramManager.convertTreeToDiagram(chosenFiles);
            classDiagramManager.arrangeDiagram();
            File actualFile = classDiagramManager.exportDiagramToGraphML(Paths.get(String.format("%s%s%s",
                                                                                                 PathConstructor.getCurrentPath(),
                                                                                                 File.separator,
                                                                                                 PathConstructor.constructPath("src",
                                                                                                                               "test",
                                                                                                                               "resources",
                                                                                                                               "testingExportedFile.graphML"))));

            DiagramArrangementManager classDiagramArrangement = new ClassDiagramArrangementManager(classDiagramManager.getClassDiagram());
            classDiagramManager.getClassDiagram().setGraphMLDiagramGeometry(classDiagramArrangement.arrangeGraphMLDiagram());
            GraphMLClassifierVertex graphMLClassifierVertex = new GraphMLClassifierVertex(classDiagramManager.getClassDiagram());
            graphMLClassifierVertex.convertSinkVertex();
            GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc(classDiagramManager.getClassDiagram());
            graphMLClassifierVertexArc.convertSinkVertexArc();

            DiagramExporter graphMLExporter = new GraphMLClassDiagramExporter(classDiagramManager.getClassDiagram());
            File expectedFile = graphMLExporter.exportDiagram(Paths.get(String.format("%s%s%s",
                                                                                      PathConstructor.getCurrentPath(),
                                                                                      File.separator,
                                                                                      PathConstructor.constructPath("src",
                                                                                                                    "test",
                                                                                                                    "resources",
                                                                                                                    "testingExportedFile.graphML"))));
            assertTrue(FileUtils.contentEquals(expectedFile, actualFile));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Test
    void saveDiagramTest()
    {
        try
        {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow",
                                                     "LatexEditorView",
                                                     "OpeningWindow");
            classDiagramManager.createSourceProject(LatexEditor.SRC.path);
            classDiagramManager.convertTreeToDiagram(chosenFiles);

            File testingSavedFile = classDiagramManager.saveDiagram(Paths.get(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "testingSavedFile.txt"))));
            DiagramExporter javaFXExporter = new JavaFXClassDiagramExporter(classDiagramManager.getClassDiagram());
            assertTrue(FileUtils.contentEquals(javaFXExporter.exportDiagram(Paths.get(String.format("%s%s%s",
                                                                                                    PathConstructor.getCurrentPath(),
                                                                                                    File.separator,
                                                                                                    PathConstructor.constructPath("src",
                                                                                                                                  "test",
                                                                                                                                  "resources",
                                                                                                                                  "testingExportedFile.txt")))), testingSavedFile));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Test
    void loadDiagramTest()
    {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow",
                                                 "LatexEditorView",
                                                 "OpeningWindow");
        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(chosenFiles);
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> createdDiagram = classDiagramManager.getClassDiagram().getDiagram();
        classDiagramManager.saveDiagram(Paths.get(String.format("%s%s%s",
                                                                PathConstructor.getCurrentPath(),
                                                                File.separator,
                                                                PathConstructor.constructPath("src",
                                                                                              "test",
                                                                                              "resources",
                                                                                              "testingExportedFile.txt"))));
        classDiagramManager.loadDiagram(Paths.get(String.format("%s%s%s",
                                                                PathConstructor.getCurrentPath(),
                                                                File.separator,
                                                                PathConstructor.constructPath("src",
                                                                                              "test",
                                                                                              "resources",
                                                                                              "testingExportedFile.txt"))));
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> loadedDiagram = classDiagramManager.getClassDiagram().getDiagram();

        for (ClassifierVertex classifierVertex : createdDiagram.keySet())
        {
            Optional<ClassifierVertex> optionalSinkVertex = loadedDiagram.keySet()
                .stream()
                .filter(it -> it.getName().equals(classifierVertex.getName())).findFirst();
            assertTrue(optionalSinkVertex.isPresent());

            assertEquals(createdDiagram.get(classifierVertex).size(), loadedDiagram.get(optionalSinkVertex.get()).size());
            for (Arc<ClassifierVertex> arc : createdDiagram.get(classifierVertex))
            {
                assertTrue(loadedDiagram.get(optionalSinkVertex.get())
                               .stream()
                               .anyMatch(it -> it.sourceVertex().getName().equals(arc.sourceVertex().getName()) &&
                                               it.targetVertex().getName().equals(arc.targetVertex().getName()) &&
                                               it.arcType().equals(arc.arcType())));
            }
        }
    }
}
