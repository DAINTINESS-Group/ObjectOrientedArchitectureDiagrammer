package model.diagram;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.graphml.GraphMLClassExporter;
import model.diagram.javafx.classdiagram.JavaFXClassDiagramExporter;
import model.diagram.javafx.classdiagram.JavaFXClassDiagramLoader;
import model.diagram.javafx.classdiagram.JavaFXClassVisualization;
import model.diagram.plantuml.PlantUMLClassExporter;
import model.diagram.plantuml.PlantUMLExportType;
import model.graph.Arc;
import model.graph.SinkVertex;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class ClassDiagram {

    private Map<Integer, List<Double>> nodesGeometry;
    private Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;
    private final Map<SinkVertex, Integer> graphNodes;
    private final Map<Arc<SinkVertex>, Integer> graphEdges;
    private Map<Path, SinkVertex> sinkVertices;

    public ClassDiagram() {
        diagram = new HashMap<>();
        graphNodes = new HashMap<>();
        graphEdges = new HashMap<>();
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> createDiagram(List<String> chosenFileNames) {
        createNodeCollection(chosenFileNames);
        createEdgeCollection();
        diagram = convertCollectionsToDiagram();
        return diagram;
    }

    public Map<Integer, List<Double>> arrangeDiagram() {
        DiagramArrangement diagramArrangement = new DiagramArrangement();
        nodesGeometry = diagramArrangement.arrangeClassDiagram(graphNodes, graphEdges);
        return nodesGeometry;
    }

    public File exportDiagramToGraphML(Path graphMLSavePath) {
        GraphMLClassExporter graphMLExporter = new GraphMLClassExporter(graphNodes, nodesGeometry, graphEdges);
        return graphMLExporter.exportDiagramToGraphML(graphMLSavePath);
    }

    public File saveDiagram(Path graphSavePath) {
        JavaFXClassDiagramExporter javaFXClassDiagramExporter = new JavaFXClassDiagramExporter(graphSavePath, diagram);
        return javaFXClassDiagramExporter.saveDiagram();
    }

    public Set<SinkVertex> loadDiagram(Path graphSavePath) {
        JavaFXClassDiagramLoader javaFXClassDiagramLoader =  new JavaFXClassDiagramLoader(graphSavePath);
        return javaFXClassDiagramLoader.loadDiagram();
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> convertCollectionsToDiagram() {
        GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
        return graphClassDiagramConverter.convertGraphToClassDiagram();
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXClassVisualization javaFXVisualization = new JavaFXClassVisualization(diagram);
        return javaFXVisualization.createGraphView();
    }

    public File exportPlantUML(Path fileSavePth, PlantUMLExportType exportType) {
        PlantUMLClassExporter plantUMLClassExporter = new PlantUMLClassExporter(fileSavePth, graphNodes, graphEdges);

        if (exportType.equals(PlantUMLExportType.TEXT)) {
            return plantUMLClassExporter.exportClassDiagramText();
        }else {
            return plantUMLClassExporter.exportClassDiagram();
        }
    }

    public void setSinkVertices(Map<Path, SinkVertex> sinkVertices) {
        this.sinkVertices = sinkVertices;
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
            Optional<SinkVertex> optionalSinkVertex = sinkVertices.values().stream().
                    filter(sinkVertex -> sinkVertex.getName().equals(chosenClass)).findFirst();
            if (optionalSinkVertex.isEmpty()) {
                continue;
            }
            chosenClasses.add(optionalSinkVertex.get());
        }
        return chosenClasses;
    }

    public Map<SinkVertex, Integer> getGraphNodes() {
        return graphNodes;
    }

    public Map<Arc<SinkVertex>, Integer> getGraphEdges() {
        return graphEdges;
    }

}
