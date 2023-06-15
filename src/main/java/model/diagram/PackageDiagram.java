package model.diagram;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.arrangement.DiagramArrangement;
import model.diagram.arrangement.PackageDiagramArrangement;
import model.diagram.graphml.GraphMLPackageDiagramExporter;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.javafx.packagediagram.JavaFXPackageDiagramExporter;
import model.diagram.javafx.packagediagram.JavaFXPackageDiagramLoader;
import model.diagram.javafx.packagediagram.JavaFXPackageVisualization;
import model.diagram.plantuml.PlantUMLPackageDiagramImageExporter;
import model.diagram.plantuml.PlantUMLPackageDiagramTextExporter;
import model.graph.Arc;
import model.graph.Vertex;
import org.javatuples.Pair;

import java.nio.file.Path;
import java.util.*;

public class PackageDiagram {

    private final Map<Vertex, Integer> graphNodes;
    private final Map<Arc<Vertex>, Integer> graphEdges;
    private Map<Path, Vertex> vertices;
    private Map<Integer, Pair<Double, Double>> nodesGeometry;

    public PackageDiagram() {
        graphNodes = new HashMap<>();
        graphEdges = new HashMap<>();
    }

    public Map<Vertex, Set<Arc<Vertex>>> createDiagram(List<String> chosenFileNames) {
        createNodeCollection(chosenFileNames);
        createEdgeCollection();
        return convertCollectionsToDiagram();
    }

    public Map<Integer, Pair<Double, Double>> arrangeDiagram() {
        DiagramArrangement diagramArrangement = new PackageDiagramArrangement(graphNodes, graphEdges);
        nodesGeometry = diagramArrangement.arrangeDiagram();
        return nodesGeometry;
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        JavaFXVisualization javaFXPackageVisualization = new JavaFXPackageVisualization(diagram);
        return javaFXPackageVisualization.createGraphView();
    }

    public DiagramExporter createGraphMLExporter() {
        return new GraphMLPackageDiagramExporter(graphNodes, nodesGeometry, graphEdges);
    }

    public DiagramExporter createJavaFXExporter(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        return new JavaFXPackageDiagramExporter(diagram);
    }

    public Map<Vertex, Set<Arc<Vertex>>> loadDiagram(Path graphSavePath) {
        JavaFXPackageDiagramLoader javaFXPackageDiagramLoader = new JavaFXPackageDiagramLoader(graphSavePath);
        Set<Vertex> loadedDiagram = javaFXPackageDiagramLoader.loadDiagram();
        GraphPackageDiagramConverter graphPackageDiagramConverter = new GraphPackageDiagramConverter(loadedDiagram);
        return graphPackageDiagramConverter.convertGraphToPackageDiagram();
    }

    public Map<Vertex, Set<Arc<Vertex>>> convertCollectionsToDiagram() {
        GraphPackageDiagramConverter graphPackageDiagramConverter = new GraphPackageDiagramConverter(graphNodes.keySet());
        return graphPackageDiagramConverter.convertGraphToPackageDiagram();
    }

    public DiagramExporter createPlantUMLImageExporter() {
        return new PlantUMLPackageDiagramImageExporter(graphNodes, graphEdges);
    }

    public DiagramExporter createPlantUMLTextExporter() {
        return new PlantUMLPackageDiagramTextExporter(graphNodes, graphEdges);
    }

    public void setVertices(Map<Path, Vertex> vertices) {
        this.vertices = vertices;
    }

    private void createNodeCollection(List<String> chosenFilesNames) {
        int nodeId = 0;
        for (Vertex vertex: getChosenNodes(chosenFilesNames)) {
            graphNodes.put(vertex, nodeId);
            nodeId++;
        }
    }

    private void createEdgeCollection() {
        int edgeId = 0;
        for (Vertex vertex: graphNodes.keySet()) {
            for (Arc<Vertex> arc: vertex.getArcs()) {
                if (!graphNodes.containsKey(arc.getTargetVertex())) {
                    continue;
                }
                graphEdges.put(arc, edgeId);
                edgeId++;
            }
        }
    }

    public Map<Vertex, Integer> getGraphNodes() {
        return graphNodes;
    }

    public Map<Arc<Vertex>, Integer> getGraphEdges() {
        return graphEdges;
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
