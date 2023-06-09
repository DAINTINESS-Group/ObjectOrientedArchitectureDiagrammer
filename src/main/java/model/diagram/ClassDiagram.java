package model.diagram;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.arrangement.ClassDiagramArrangement;
import model.diagram.arrangement.DiagramArrangement;
import model.diagram.graphml.GraphMLClassDiagramExporter;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.javafx.classdiagram.JavaFXClassDiagramExporter;
import model.diagram.javafx.classdiagram.JavaFXClassDiagramLoader;
import model.diagram.javafx.classdiagram.JavaFXClassVisualization;
import model.diagram.plantuml.PlantUMLClassDiagramImageExporter;
import model.diagram.plantuml.PlantUMLClassDiagramTextExporter;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.javatuples.Pair;

import java.nio.file.Path;
import java.util.*;

public class ClassDiagram {

    private Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;
    private final Map<SinkVertex, Integer> graphNodes;
    private final Map<Arc<SinkVertex>, Integer> graphEdges;
    private Map<Path, SinkVertex> sinkVertices;
    private Map<Integer, Pair<Double, Double>> nodesGeometry;

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

    public Map<Integer, Pair<Double, Double>> arrangeDiagram() {
        DiagramArrangement classDiagramArrangement = new ClassDiagramArrangement(graphNodes, graphEdges);
        nodesGeometry = classDiagramArrangement.arrangeDiagram();
        return nodesGeometry;
    }

    public DiagramExporter createGraphMLExporter() {
        return new GraphMLClassDiagramExporter(graphNodes, nodesGeometry, graphEdges);
    }

    public DiagramExporter createJavaFXExporter() {
        return new JavaFXClassDiagramExporter(diagram);
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> loadDiagram(Path graphSavePath) {
        JavaFXClassDiagramLoader javaFXClassDiagramLoader =  new JavaFXClassDiagramLoader(graphSavePath);
        Set<SinkVertex> loadedDiagram = javaFXClassDiagramLoader.loadDiagram();
        GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(loadedDiagram);
        return graphClassDiagramConverter.convertGraphToClassDiagram();
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> convertCollectionsToDiagram() {
        GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
        return graphClassDiagramConverter.convertGraphToClassDiagram();
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXVisualization javaFXVisualization = new JavaFXClassVisualization(diagram);
        return javaFXVisualization.createGraphView();
    }

    public DiagramExporter createPlantUMLImageExporter() {
        return new PlantUMLClassDiagramImageExporter(graphNodes, graphEdges);
    }

    public DiagramExporter createPlantUMLTextExporter() {
        return new PlantUMLClassDiagramTextExporter(graphNodes, graphEdges);
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
