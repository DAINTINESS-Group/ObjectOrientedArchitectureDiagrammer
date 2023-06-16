package model.diagram;

import model.diagram.arrangement.DiagramArrangement;
import model.diagram.arrangement.PackageDiagramArrangement;
import model.diagram.exportation.*;
import model.graph.Arc;
import model.graph.Vertex;
import org.javatuples.Pair;

import java.nio.file.Path;
import java.util.*;

public class PackageDiagram {

    private final Map<Vertex, Integer> graphNodes;
    private Map<Path, Vertex> vertices;

    public PackageDiagram() {
        graphNodes = new HashMap<>();
    }

    public Map<Vertex, Integer> createGraphNodes(List<String> chosenFileNames) {
        int nodeId = 0;
        for (Vertex vertex: getChosenNodes(chosenFileNames)) {
            graphNodes.put(vertex, nodeId);
            nodeId++;
        }
        return graphNodes;
    }

    public void setVertices(Map<Path, Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Vertex> getChosenNodes(List<String> chosenPackagesNames) {
        List<Vertex> chosenPackages = new ArrayList<>();
        for (String chosenPackage: chosenPackagesNames) {
            Optional<Vertex> vertex = vertices.values().stream()
                .filter(vertex1 -> vertex1.getName().equals(chosenPackage))
                .findFirst();
            if (vertex.isEmpty()) {
                continue;
            }
            chosenPackages.add(vertex.get());
        }
        return chosenPackages;
    }
}
