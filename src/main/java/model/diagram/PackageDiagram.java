package model.diagram;

import model.graph.Arc;
import model.graph.PackageVertex;
import org.javatuples.Pair;

import java.nio.file.Path;
import java.util.*;

public class PackageDiagram {

    private Map<PackageVertex, Set<Arc<PackageVertex>>> diagram;
    private Map<Path, PackageVertex> vertices;
    private final Map<PackageVertex, Integer> graphNodes;
    private Map<Integer, Pair<Double, Double>> diagramGeometry;

    public PackageDiagram() {
        graphNodes = new HashMap<>();
    }

    public void createNewDiagram(List<String> chosenFileNames) {
        createGraphNodes(chosenFileNames);
        createDiagram(graphNodes.keySet());
    }

    public void createDiagram(Set<PackageVertex> vertices) {
        GraphPackageDiagramConverter packageDiagramConverter = new GraphPackageDiagramConverter(vertices);
        diagram = packageDiagramConverter.convertGraphToPackageDiagram();
    }

    private void createGraphNodes(List<String> chosenFileNames) {
        int nodeId = 0;
        for (PackageVertex vertex: getChosenNodes(chosenFileNames)) {
            graphNodes.put(vertex, nodeId);
            nodeId++;
        }
    }

    public List<PackageVertex> getChosenNodes(List<String> chosenPackagesNames) {
        List<PackageVertex> chosenPackages = new ArrayList<>();
        for (String chosenPackage: chosenPackagesNames) {
            Optional<PackageVertex> vertex = vertices.values().stream()
                .filter(vertex1 -> vertex1.getName().equals(chosenPackage))
                .findFirst();
            if (vertex.isEmpty()) {
                continue;
            }
            chosenPackages.add(vertex.get());
        }
        return chosenPackages;
    }

    public void setVertices(Map<Path, PackageVertex> vertices) {
        this.vertices = vertices;
    }

    public void setDiagramGeometry(Map<Integer, Pair<Double, Double>> diagramGeometry) {
        this.diagramGeometry = diagramGeometry;
    }

    public Map<PackageVertex, Integer> getGraphNodes() {
        return graphNodes;
    }

    public Map<PackageVertex, Set<Arc<PackageVertex>>> getDiagram() {
        return diagram;
    }

    public Map<Integer, Pair<Double, Double>> getDiagramGeometry() {
        return diagramGeometry;
    }

}
