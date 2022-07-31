package model.diagram;

import model.diagram.javafx.JavaFXExporter;
import model.diagram.graphml.GraphMLExporter;
import model.tree.Node;
import model.tree.SourceProject;

import java.io.*;
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

    public SourceProject createTree(String sourcePackagePath) {
        sourceProject = new SourceProject(sourcePackagePath);
        sourceProject.parseSourceProject();
        return sourceProject;
    }

    public Map<String, Map<String, String>> createDiagram(List<String> chosenFilesNames){
        graphNodeCollection.populateGraphNodes(getChosenNodes(chosenFilesNames));
        graphEdgeCollection.setGraphNodes(graphNodeCollection.getGraphNodes());
        graphEdgeCollection.populateGraphEdges(getChosenNodes(chosenFilesNames));
        createdDiagram = convertCollectionsToDiagram();
        return createdDiagram;
    }

    public Map<Integer, List<Double>> arrangeDiagram(){
        DiagramArrangement diagramArrangement = new DiagramArrangement();
        nodesGeometry = diagramArrangement.arrangeDiagram(graphNodeCollection.getGraphNodes(), graphEdgeCollection.getGraphEdges());
        return nodesGeometry;
    }

    public File exportDiagramToGraphML(String graphMLSavePath){
        graphNodeCollection.convertNodesToGraphML(nodesGeometry);
        graphEdgeCollection.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        return graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphNodeCollection.getGraphMLBuffer(), graphEdgeCollection.getGraphMLBuffer());
    }

    public File saveDiagram(String graphSavePath) {
        JavaFXExporter javaFXExporter = new JavaFXExporter();
        return javaFXExporter.saveDiagram(createdDiagram, graphSavePath);
    }

    public Map<String, Map<String, String>> loadDiagram(String graphSavePath) {
        JavaFXExporter javaFXExporter = new JavaFXExporter();
        createdDiagram = javaFXExporter.loadDiagram(graphSavePath);
        return createdDiagram;
    }

    public Map<String, Map<String, String>> convertCollectionsToDiagram() {
        CollectionsDiagramConverter collectionsDiagramConverter = new CollectionsDiagramConverter(graphNodeCollection, graphEdgeCollection);
        return collectionsDiagramConverter.convertCollectionsToDiagram();
    }

    public abstract List<Node> getChosenNodes(List<String> chosenFileNames);

}
