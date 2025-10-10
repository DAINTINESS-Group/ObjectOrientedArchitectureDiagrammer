package gr.uoi.ooad.model.diagram;

import java.nio.file.Path;
import java.util.*;
import gr.uoi.ooad.model.diagram.arrangement.geometry.DiagramGeometry;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.PackageVertex;
import org.javatuples.Pair;

public class PackageDiagram {

    private final Map<PackageVertex, Integer> graphNodes;
    private Map<PackageVertex, Set<Arc<PackageVertex>>> diagram;
    private Map<Path, PackageVertex> vertices;
    private Map<Integer, Pair<Double, Double>> diagramGeometryGraphML;
    private DiagramGeometry diagramGeometry;

    public PackageDiagram() {
        this.graphNodes = new HashMap<>();
    }

    public void createNewDiagram(List<String> chosenFileNames) {
        createGraphNodes(chosenFileNames);
        createDiagram(graphNodes.keySet());
    }

    public void createDiagram(Set<PackageVertex> vertices) {
        GraphPackageDiagramConverter packageDiagramConverter =
                new GraphPackageDiagramConverter(vertices);
        diagram = packageDiagramConverter.convertGraphToPackageDiagram();
    }

    private void createGraphNodes(List<String> chosenFileNames) {
        int nodeId = 0;
        List<PackageVertex> chosenNodes = getChosenNodes(chosenFileNames);
        for (PackageVertex vertex : chosenNodes) {
            graphNodes.put(vertex, nodeId++);
        }
    }

    public List<PackageVertex> getChosenNodes(List<String> chosenPackagesNames) {
        List<PackageVertex> chosenPackages = new ArrayList<>();
        for (String chosenPackage : chosenPackagesNames) {
            vertices.values().stream()
                    .filter(it -> it.getName().equals(chosenPackage))
                    .findFirst()
                    .ifPresent(chosenPackages::add);
        }

        return chosenPackages;
    }

    public void setVertices(Map<Path, PackageVertex> vertices) {
        this.vertices = vertices;
    }

    public void setGraphMLDiagramGeometry(
            Map<Integer, Pair<Double, Double>> diagramGeometryGraphML) {
        this.diagramGeometryGraphML = diagramGeometryGraphML;
    }

    public void setDiagramGeometry(DiagramGeometry diagramGeometry) {
        this.diagramGeometry = diagramGeometry;
    }

    public Map<PackageVertex, Integer> getGraphNodes() {
        return graphNodes;
    }

    public Map<PackageVertex, Set<Arc<PackageVertex>>> getDiagram() {
        return diagram;
    }

    public Map<Integer, Pair<Double, Double>> getGraphMLDiagramGeometry() {
        return diagramGeometryGraphML;
    }

    public DiagramGeometry getDiagramGeometry() {
        return diagramGeometry;
    }
}
