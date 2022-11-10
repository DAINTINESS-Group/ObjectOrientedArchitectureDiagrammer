package manager.diagram;

import manager.ClassDiagramManager;
import manager.DiagramManager;
import model.diagram.CollectionsDiagramConverter;
import model.diagram.DiagramArrangement;
import model.diagram.GraphEdgeCollection;
import model.diagram.GraphNodeCollection;
import model.diagram.graphml.GraphMLExporter;
import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.diagram.javafx.JavaFXExporter;
import model.tree.LeafNode;
import model.tree.Node;
import model.tree.PackageNode;
import model.tree.SourceProject;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ClassDiagramManagerTest {

    Path currentDirectory = Path.of(".");

    @Test
    void createTreeTest() throws IOException {
        DiagramManager classDiagramManager = new ClassDiagramManager();

        SourceProject sourceProject = new SourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        sourceProject.parseSourceProject();
        SourceProject testingSourceProject = classDiagramManager.createTree(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        Map<Path, PackageNode> packageNodes = sourceProject.getPackageNodes();
        Map<Path, PackageNode> testingPackageNodes = testingSourceProject.getPackageNodes();

        for (Map.Entry<Path, PackageNode> entry: packageNodes.entrySet()) {
            assertEquals(entry.getValue().getName(), testingPackageNodes.get(entry.getKey()).getName());
            assertEquals(entry.getValue().getParentNode().getName(), testingPackageNodes.get(entry.getKey()).getParentNode().getName());
            for (Map.Entry<String, LeafNode> leafNodeEntry: entry.getValue().getLeafNodes().entrySet()) {
                assertEquals(leafNodeEntry.getValue().getName(), testingPackageNodes.get(entry.getKey()).getLeafNodes().get(leafNodeEntry.getKey()).getName());
            }
        }
    }

    @Test
    void createDiagramTest() throws IOException {
        DiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
        SourceProject sourceProject = classDiagramManager.createTree(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        Map<String, Map<String, String>> testingCreatedDiagram = classDiagramManager.createDiagram(chosenFiles);

        GraphNodeCollection graphNodeCollection = new GraphMLLeafNode();
        GraphEdgeCollection graphEdgeCollection = new GraphMLLeafEdge();
        graphNodeCollection.populateGraphNodes(getChosenNodes(chosenFiles, sourceProject));
        graphEdgeCollection.setGraphNodes(graphNodeCollection.getGraphNodes());
        graphEdgeCollection.populateGraphEdges(getChosenNodes(chosenFiles, sourceProject));

        CollectionsDiagramConverter collectionsDiagramConverter = new CollectionsDiagramConverter(graphNodeCollection, graphEdgeCollection);
        Map<String, Map<String, String>> createdDiagram = collectionsDiagramConverter.convertCollectionsToDiagram();

        for (Map.Entry<String, Map<String, String>> entry: createdDiagram.entrySet()) {
            assertTrue(testingCreatedDiagram.containsKey(entry.getKey()));
            for (Map.Entry<String, String> entry1: entry.getValue().entrySet()) {
                assertTrue(testingCreatedDiagram.get(entry.getKey()).containsKey(entry1.getKey()));
                assertEquals(entry1.getValue(), testingCreatedDiagram.get(entry.getKey()).get(entry1.getKey()));
            }
        }
    }

    @Test
    void exportDiagramToGraphMLTest() throws IOException {
        DiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
        SourceProject sourceProject = classDiagramManager.createTree(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        classDiagramManager.createDiagram(chosenFiles);
        classDiagramManager.arrangeDiagram();

        File testingExportedFile = classDiagramManager.exportDiagramToGraphML(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));

        GraphNodeCollection graphNodeCollection = new GraphMLLeafNode();
        GraphEdgeCollection graphEdgeCollection = new GraphMLLeafEdge();
        graphNodeCollection.populateGraphNodes(getChosenNodes(chosenFiles, sourceProject));
        graphEdgeCollection.setGraphNodes(graphNodeCollection.getGraphNodes());
        graphEdgeCollection.populateGraphEdges(getChosenNodes(chosenFiles, sourceProject));

        DiagramArrangement diagramArrangement = new DiagramArrangement();
        Map<Integer, List<Double>> nodesGeometry = diagramArrangement.arrangeDiagram(graphNodeCollection.getGraphNodes(), graphEdgeCollection.getGraphEdges());

        graphNodeCollection.convertNodesToGraphML(nodesGeometry);
        graphEdgeCollection.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();

        try {
            assertTrue(FileUtils.contentEquals(graphMLExporter.exportDiagramToGraphML(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"),
                    graphNodeCollection.getGraphMLBuffer(), graphEdgeCollection.getGraphMLBuffer()), testingExportedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveDiagramTest() throws IOException {
        DiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
        //SourceProject sourceProject = 
        		classDiagramManager.createTree(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        Map<String, Map<String, String>> createdDiagram = classDiagramManager.createDiagram(chosenFiles);

        File testingSavedFile = classDiagramManager.saveDiagram(Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));
        JavaFXExporter javaFXExporter = new JavaFXExporter();

        try {
            assertTrue(FileUtils.contentEquals(javaFXExporter.saveDiagram(createdDiagram,
                    Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML")), testingSavedFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadDiagramTest() throws IOException {
        DiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
        //SourceProject sourceProject = 
        		classDiagramManager.createTree(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        Map<String, Map<String, String>> createdDiagram = classDiagramManager.createDiagram(chosenFiles);
        //File testingSavedFile = 
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
