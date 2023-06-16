package model.diagram;

import model.diagram.arrangement.ClassDiagramArrangement;
import model.diagram.arrangement.DiagramArrangement;
import model.diagram.exportation.*;
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

    public Map<SinkVertex, Integer> createGraphNodes(List<String> chosenFileNames) {
        int nodeId = 0;
        for (SinkVertex sinkVertex: getChosenNodes(chosenFileNames)) {
            graphNodes.put(sinkVertex, nodeId);
            nodeId++;
        }
        return graphNodes;

    }

    public void setSinkVertices(Map<Path, SinkVertex> sinkVertices) {
        this.sinkVertices = sinkVertices;
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

}
