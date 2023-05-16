package model.diagram;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.javafx.JavaFXExporter;
import model.diagram.javafx.JavaFXLoader;
import model.diagram.graphml.GraphMLExporter;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.plantuml.PlantUMLExporter;
import model.tree.Node;
import model.tree.SourceProject;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Diagram {

    protected Map<Integer, List<Double>> nodesGeometry;
    protected GraphNodeCollection graphNodeCollection;
    protected GraphEdgeCollection graphEdgeCollection;
    protected SourceProject sourceProject;
    private Map<String, Map<String, String>> createdDiagram;

    public Diagram() {
        this.createdDiagram = new HashMap<>();
    }

    public SourceProject createSourceProject(Path sourcePackagePath) {
        sourceProject = new SourceProject();
        return sourceProject;
    }

    public Map<String, Map<String, String>> createDiagram(List<String> chosenFilesNames) {
        createCollections();
        graphNodeCollection.populateGraphNodes(getChosenNodes(chosenFilesNames));
        graphEdgeCollection.setGraphNodes(graphNodeCollection.getGraphNodes());
        graphEdgeCollection.populateGraphEdges(getChosenNodes(chosenFilesNames));
        createdDiagram = convertCollectionsToDiagram();
        return createdDiagram;
    }

    public Map<Integer, List<Double>> arrangeDiagram() {
        DiagramArrangement diagramArrangement = new DiagramArrangement();
        nodesGeometry = diagramArrangement.arrangeDiagram(graphNodeCollection.getGraphNodes(), graphEdgeCollection.getGraphEdges());
        return nodesGeometry;
    }

    public File exportDiagramToGraphML(Path graphMLSavePath) {
        graphNodeCollection.convertNodesToGraphML(nodesGeometry);
        graphEdgeCollection.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        return graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphNodeCollection.getGraphMLBuffer(), graphEdgeCollection.getGraphMLBuffer());
    }
    
    public void exportPlantUMLDiagram(Path graphSavePath) {
    	boolean packageDiagram = false;
    	graphNodeCollection.convertNodesToPlantUML();
    	packageDiagram = graphNodeCollection.getDiagramsChoice();
    	graphEdgeCollection.convertEdgesToPlantUML();
    	PlantUMLExporter plantUMLExporter = new PlantUMLExporter();
    	plantUMLExporter.exportDiagram(graphSavePath, graphNodeCollection.getPlantUMLBuffer(), graphEdgeCollection.getPlantUMLBuffer(), packageDiagram);
    }
    
    public void exportPlantUMLText(Path textSavePath) {
    	boolean packageDiagram = false;
    	graphNodeCollection.convertNodesToPlantUML();
    	packageDiagram = graphNodeCollection.getDiagramsChoice();
    	graphEdgeCollection.convertEdgesToPlantUML();
    	PlantUMLExporter plantUMLExporter = new PlantUMLExporter();
    	plantUMLExporter.exportText(textSavePath, graphNodeCollection.getPlantUMLBuffer(), graphEdgeCollection.getPlantUMLBuffer(), packageDiagram);
    }
    
    public File saveDiagram(Path graphSavePath) {
        JavaFXExporter javaFXExporter = new JavaFXExporter();
        return javaFXExporter.saveDiagram(createdDiagram, graphSavePath);
    }

    public Map<String, Map<String, String>> loadDiagram(Path graphSavePath) {
        JavaFXLoader javaFXLoader = new JavaFXLoader();
        createdDiagram = javaFXLoader.loadDiagram(graphSavePath);
        return createdDiagram;
    }

    public Map<String, Map<String, String>> convertCollectionsToDiagram() {
        CollectionsDiagramConverter collectionsDiagramConverter = new CollectionsDiagramConverter(graphNodeCollection, graphEdgeCollection);
        return collectionsDiagramConverter.convertCollectionsToDiagram();
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXVisualization javaFXVisualization = new JavaFXVisualization();
        return javaFXVisualization.createGraphView(createdDiagram);
    }

    public abstract List<Node> getChosenNodes(List<String> chosenFileNames);

    public abstract void createCollections();

}
