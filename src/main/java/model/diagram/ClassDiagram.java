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

    private final Map<SinkVertex, Integer> graphNodes;
    private Map<Path, SinkVertex> sinkVertices;

    public ClassDiagram() {
        graphNodes = new HashMap<>();
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> createDiagram(List<String> chosenFileNames) {
        createNodeCollection(chosenFileNames);
        return convertCollectionsToDiagram(graphNodes.keySet());
    }

    public Map<Integer, Pair<Double, Double>> arrangeDiagram(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        DiagramArrangement classDiagramArrangement = new ClassDiagramArrangement(graphNodes, diagram);
        return classDiagramArrangement.arrangeDiagram();
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        JavaFXVisualization javaFXVisualization = new JavaFXClassVisualization(diagram);
        return javaFXVisualization.createGraphView();
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> loadDiagram(Path graphSavePath) {
        JavaFXClassDiagramLoader javaFXClassDiagramLoader =  new JavaFXClassDiagramLoader(graphSavePath);
        Set<SinkVertex> loadedDiagram = javaFXClassDiagramLoader.loadDiagram();
        return convertCollectionsToDiagram(loadedDiagram);
    }

    public DiagramExporter createGraphMLExporter(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram, Map<Integer, Pair<Double, Double>> diagramGeometry) {
        return new GraphMLClassDiagramExporter(graphNodes, diagramGeometry, diagram);
    }

    public DiagramExporter createJavaFXExporter(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        return new JavaFXClassDiagramExporter(diagram);
    }

    public DiagramExporter createPlantUMLImageExporter(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        return new PlantUMLClassDiagramImageExporter(graphNodes, diagram);
    }

    public DiagramExporter createPlantUMLTextExporter(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        return new PlantUMLClassDiagramTextExporter(graphNodes, diagram);
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

    private List<SinkVertex> getChosenNodes(List<String> chosenClassesNames) {
        List<SinkVertex> chosenClasses = new ArrayList<>();
        for (String chosenClass: chosenClassesNames) {
            Optional<SinkVertex> optionalSinkVertex = sinkVertices.values().stream()
                .filter(sinkVertex -> sinkVertex.getName().equals(chosenClass))
                .findFirst();
            if (optionalSinkVertex.isEmpty()) {
                continue;
            }
            chosenClasses.add(optionalSinkVertex.get());
        }
        return chosenClasses;
    }

    private Map<SinkVertex, Set<Arc<SinkVertex>>> convertCollectionsToDiagram(Set<SinkVertex> sinkVertices) {
        GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(sinkVertices);
        return graphClassDiagramConverter.convertGraphToClassDiagram();
    }

    public Map<SinkVertex, Integer> getGraphNodes() {
        return graphNodes;
    }

}
