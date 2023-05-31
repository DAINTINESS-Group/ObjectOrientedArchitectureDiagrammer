package model.diagram;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.SourceProject;
import model.diagram.graphml.GraphMLExporter;
import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.diagram.javafx.JavaFXExporter;
import model.diagram.javafx.JavaFXLoader;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.plantuml.PlantUMLLeafEdge;
import model.diagram.plantuml.PlantUMLExportType;
import model.diagram.plantuml.PlantUMLExporter;
import model.diagram.plantuml.PlantUMLLeafNode;
import model.graph.Arc;
import model.graph.SinkVertex;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class ClassDiagram {

    private Map<Integer, List<Double>> nodesGeometry;
    private SourceProject sourceProject;
    private Map<String, Map<String, String>> createdDiagram;
    private final Map<SinkVertex, Integer> graphNodes;
    private final Map<Arc<SinkVertex>, Integer> graphEdges;

    public ClassDiagram() {
        createdDiagram = new HashMap<>();
        graphNodes = new HashMap<>();
        graphEdges = new HashMap<>();
    }

    public Map<String, Map<String, String>> createDiagram(List<String> chosenFileNames) {
        createNodeCollection(chosenFileNames);
        createEdgeCollection();
        createdDiagram = convertCollectionsToDiagram();
        return createdDiagram;
    }

    public Map<Integer, List<Double>> arrangeDiagram() {
        DiagramArrangement diagramArrangement = new DiagramArrangement();
        nodesGeometry = diagramArrangement.arrangeClassDiagram(graphNodes, graphEdges);
        return nodesGeometry;
    }

    public File exportDiagramToGraphML(Path graphMLSavePath) {
        GraphMLLeafNode graphMLLeafNode = new GraphMLLeafNode(graphNodes, nodesGeometry);
        StringBuilder graphMLNodeBuffer = graphMLLeafNode.convertLeafNode();
        GraphMLLeafEdge graphMLLeafEdge = new GraphMLLeafEdge(graphNodes);
        StringBuilder graphMLEdgeBuffer = graphMLLeafEdge.convertLeafEdge(graphEdges);

        GraphMLExporter graphMLExporter = new GraphMLExporter();
        return graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphMLNodeBuffer, graphMLEdgeBuffer);
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
        CollectionsDiagramConverter collectionsDiagramConverter = new CollectionsDiagramConverter();
        return collectionsDiagramConverter.convertClassCollectionsToDiagram(graphNodes, graphEdges);
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXVisualization javaFXVisualization = new JavaFXVisualization();
        return javaFXVisualization.createGraphView(createdDiagram);
    }

    public File exportPlantUML(Path fileSavePth, PlantUMLExportType exportType) {
        PlantUMLLeafNode plantUMLLeafNode = new PlantUMLLeafNode(graphNodes);
        StringBuilder plantUMLNodeBuffer = plantUMLLeafNode.convertPlantLeafNode();
        PlantUMLLeafEdge plantUMLEdge = new PlantUMLLeafEdge(graphEdges);
        StringBuilder plantUMLEdgeBuffer = plantUMLEdge.convertPlantEdge();

        PlantUMLExporter plantUMLExporter = new PlantUMLExporter(fileSavePth, plantUMLNodeBuffer, plantUMLEdgeBuffer);

        if (exportType.equals(PlantUMLExportType.TEXT)) {
            return plantUMLExporter.exportClassDiagramText();
        }else {
            return plantUMLExporter.exportClassDiagram();
        }
    }

    private void createNodeCollection(List<String> chosenFilesNames) {
        int nodeId = 0;
        for (SinkVertex sinkVertex: getChosenNodes(chosenFilesNames)) {
            graphNodes.put(sinkVertex, nodeId);
            nodeId++;
        }
    }

    private void createEdgeCollection() {
        int edgeId = 0;
        for (SinkVertex sinkVertex: graphNodes.keySet()) {
            for (Arc<SinkVertex> arc: sinkVertex.getArcs()) {
                if (!graphNodes.containsKey(arc.getTargetVertex())) {
                    continue;
                }
                graphEdges.put(arc, edgeId);
                edgeId++;
            }
        }
    }

    private List<SinkVertex> getChosenNodes(List<String> chosenClassesNames) {
        List<SinkVertex> chosenClasses = new ArrayList<>();
        for (String chosenClass: chosenClassesNames) {
            Optional<SinkVertex> optionalSinkVertex = sourceProject.getSinkVertices().values().stream().
                    filter(sinkVertex -> sinkVertex.getName().equals(chosenClass)).findFirst();
            if (optionalSinkVertex.isEmpty()) {
                continue;
            }
            chosenClasses.add(optionalSinkVertex.get());
        }
        return chosenClasses;
    }

    public void setSourceProject(SourceProject sourceProject) {
        this.sourceProject = sourceProject;
    }

    public Map<SinkVertex, Integer> getGraphNodes() {
        return graphNodes;
    }

    public Map<Arc<SinkVertex>, Integer> getGraphEdges() {
        return graphEdges;
    }

}
