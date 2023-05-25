package model.diagram;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.javafx.JavaFXExporter;
import model.diagram.javafx.JavaFXLoader;
import model.diagram.graphml.GraphMLExporter;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.plantuml.PlantUMLExportType;
import model.diagram.plantuml.PlantUMLExporter;
import model.tree.node.Node;
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

    public SourceProject createSourceProject() {
        sourceProject = new SourceProject();
        return sourceProject;
    }

    public Map<String, Map<String, String>> createDiagram(List<String> chosenFilesNames) {
        graphNodeCollection = new GraphNodeCollection();
        graphEdgeCollection = new GraphEdgeCollection(graphNodeCollection.getGraphNodes());
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
        convertNodesToGraphML(nodesGeometry);
        convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        return graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphNodeCollection.getGraphMLBuffer(), graphEdgeCollection.getGraphMLBuffer());
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

    public File exportPlantUML(Path fileSavePth, PlantUMLExportType exportType) {
        graphNodeCollection.convertClassNodesToPlantUML();
        graphEdgeCollection.convertEdgesToPlantUML();
        PlantUMLExporter plantUMLExporter = new PlantUMLExporter(fileSavePth, graphNodeCollection.getPlantUMLBuffer(),
                graphEdgeCollection.getPlantUMLBuffer());

        if (exportType.equals(PlantUMLExportType.TEXT)) {
            return exportPlantUMLText(plantUMLExporter);
        }else {
            return exportPlantUMLDiagram(plantUMLExporter);
        }
    }

    public abstract List<Node> getChosenNodes(List<String> chosenFileNames);

    protected abstract StringBuilder convertEdgesToGraphML();

    protected abstract StringBuilder convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry);

	public abstract File exportPlantUMLDiagram(PlantUMLExporter plantUMLExporter);

	public abstract File exportPlantUMLText(PlantUMLExporter plantUMLExporter);
}
