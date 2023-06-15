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
    private Map<Path, Vertex> vertices;

    public PackageDiagram() {
        graphNodes = new HashMap<>();
    }

    public Map<Vertex, Set<Arc<Vertex>>> createDiagram(List<String> chosenFileNames) {
        int nodeId = 0;
        for (Vertex vertex: getChosenNodes(chosenFileNames)) {
            graphNodes.put(vertex, nodeId);
            nodeId++;
        }
        return convertCollectionsToDiagram(graphNodes.keySet());
    }

    public Map<Integer, Pair<Double, Double>> arrangeDiagram(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        DiagramArrangement diagramArrangement = new PackageDiagramArrangement(graphNodes, diagram);
        return diagramArrangement.arrangeDiagram();
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        JavaFXVisualization javaFXPackageVisualization = new JavaFXPackageVisualization(diagram);
        return javaFXPackageVisualization.createGraphView();
    }

    public Map<Vertex, Set<Arc<Vertex>>> loadDiagram(Path graphSavePath) {
        JavaFXPackageDiagramLoader javaFXPackageDiagramLoader = new JavaFXPackageDiagramLoader(graphSavePath);
        Set<Vertex> loadedDiagram = javaFXPackageDiagramLoader.loadDiagram();
        return convertCollectionsToDiagram(loadedDiagram);
    }

    public DiagramExporter createGraphMLExporter(Map<Vertex, Set<Arc<Vertex>>> diagram, Map<Integer, Pair<Double, Double>> diagramGeometry) {
        return new GraphMLPackageDiagramExporter(graphNodes, diagramGeometry, diagram);
    }

    public DiagramExporter createJavaFXExporter(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        return new JavaFXPackageDiagramExporter(diagram);
    }

    public DiagramExporter createPlantUMLImageExporter(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        return new PlantUMLPackageDiagramImageExporter(diagram);
    }

    public DiagramExporter createPlantUMLTextExporter(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        return new PlantUMLPackageDiagramTextExporter(diagram);
    }

    public void setVertices(Map<Path, Vertex> vertices) {
        this.vertices = vertices;
    }

    private Map<Vertex, Set<Arc<Vertex>>> convertCollectionsToDiagram(Set<Vertex> vertices) {
        GraphPackageDiagramConverter graphPackageDiagramConverter = new GraphPackageDiagramConverter(vertices);
        return graphPackageDiagramConverter.convertGraphToPackageDiagram();
    }

    public Map<Vertex, Integer> getGraphNodes() {
        return graphNodes;
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
