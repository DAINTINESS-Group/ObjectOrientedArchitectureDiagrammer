package model.diagram;

import model.graph.Arc;
import model.graph.SinkVertex;
import org.javatuples.Pair;

import java.nio.file.Path;
import java.util.*;

public class ClassDiagram {

    private Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;
    private Map<Path, SinkVertex> sinkVertices;
    private final Map<SinkVertex, Integer> graphNodes;
    private Map<Integer, Pair<Double, Double>> diagramGeometry;

    public ClassDiagram() {
        graphNodes = new HashMap<>();
    }

    public void createNewDiagram(List<String> chosenFilesNames) {
        createGraphNodes(chosenFilesNames);
        createDiagram(graphNodes.keySet());
    }

    public void createDiagram(Set<SinkVertex> sinkVertices) {
        GraphClassDiagramConverter classDiagramConverter = new GraphClassDiagramConverter(sinkVertices);
        diagram = classDiagramConverter.convertGraphToClassDiagram();
    }

    private void createGraphNodes(List<String> chosenFileNames) {
        int nodeId = 0;
        for (SinkVertex sinkVertex: getChosenNodes(chosenFileNames)) {
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

    public void setSinkVertices(Map<Path, SinkVertex> sinkVertices) {
        this.sinkVertices = sinkVertices;
    }

    public void setDiagram(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        this.diagram = diagram;
    }

    public void setDiagramGeometry(Map<Integer, Pair<Double, Double>> diagramGeometry) {
        this.diagramGeometry = diagramGeometry;
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> getDiagram() {
        return diagram;
    }

    public Map<SinkVertex, Integer> getGraphNodes() {
        return graphNodes;
    }

    public Map<Integer, Pair<Double, Double>> getDiagramGeometry() {
        return diagramGeometry;
    }

}
