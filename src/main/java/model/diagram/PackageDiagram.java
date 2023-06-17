package model.diagram;

import model.graph.Arc;
import model.graph.Vertex;
import org.javatuples.Pair;

import java.nio.file.Path;
import java.util.*;

public class PackageDiagram {

    private Map<Vertex, Set<Arc<Vertex>>> diagram;
    private Map<Path, Vertex> vertices;
    private final Map<Vertex, Integer> graphNodes;
    private Map<Integer, Pair<Double, Double>> diagramGeometry;

    public PackageDiagram() {
        graphNodes = new HashMap<>();
    }

    public void createNewDiagram(List<String> chosenFileNames) {
        createGraphNodes(chosenFileNames);
        createDiagram(graphNodes.keySet());
    }

    public void createDiagram(Set<Vertex> vertices) {
        GraphPackageDiagramConverter packageDiagramConverter = new GraphPackageDiagramConverter(vertices);
        diagram = packageDiagramConverter.convertGraphToPackageDiagram();
    }

    private void createGraphNodes(List<String> chosenFileNames) {
        int nodeId = 0;
        for (Vertex vertex: getChosenNodes(chosenFileNames)) {
            graphNodes.put(vertex, nodeId);
            nodeId++;
        }
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

    public void setVertices(Map<Path, Vertex> vertices) {
        this.vertices = vertices;
    }

    public void setDiagramGeometry(Map<Integer, Pair<Double, Double>> diagramGeometry) {
        this.diagramGeometry = diagramGeometry;
    }

    public Map<Vertex, Integer> getGraphNodes() {
        return graphNodes;
    }

    public Map<Vertex, Set<Arc<Vertex>>> getDiagram() {
        return diagram;
    }

    public Map<Integer, Pair<Double, Double>> getDiagramGeometry() {
        return diagramGeometry;
    }

}
