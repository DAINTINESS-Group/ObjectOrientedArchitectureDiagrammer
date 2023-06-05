package model.diagram;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.graphml.GraphMLPackageExporter;
import model.diagram.javafx.packagediagram.JavaFXPackageDiagramExporter;
import model.diagram.javafx.packagediagram.JavaFXPackageDiagramLoader;
import model.diagram.javafx.packagediagram.JavaFXPackageVisualization;
import model.diagram.plantuml.PlantUMLExportType;
import model.diagram.plantuml.PlantUMLPackageExporter;
import model.graph.Arc;
import model.graph.Vertex;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class PackageDiagram {

    private Map<Integer, List<Double>> nodesGeometry;
    private Map<Vertex, Set<Arc<Vertex>>> diagram;
    private final Map<Vertex, Integer> graphNodes;
    private final Map<Arc<Vertex>, Integer> graphEdges;
    private Map<Path, Vertex> vertices;

    public PackageDiagram() {
        diagram = new HashMap<>();
        graphNodes = new HashMap<>();
        graphEdges = new HashMap<>();
    }

    public Map<Vertex, Set<Arc<Vertex>>> createDiagram(List<String> chosenFileNames) {
        createNodeCollection(chosenFileNames);
        createEdgeCollection();
        diagram = convertCollectionsToDiagram();
        return diagram;
    }

    public Map<Integer, List<Double>> arrangeDiagram() {
        DiagramArrangement diagramArrangement = new DiagramArrangement();
        nodesGeometry = diagramArrangement.arrangePackageDiagram(graphNodes, graphEdges);
        return nodesGeometry;
    }

    public File exportDiagramToGraphML(Path graphMLSavePath) {
        GraphMLPackageExporter graphMLPackageExporter = new GraphMLPackageExporter(graphNodes, nodesGeometry, graphEdges);
        return graphMLPackageExporter.exportDiagramToGraphML(graphMLSavePath);
    }

    public File saveDiagram(Path graphSavePath) {
        JavaFXPackageDiagramExporter javaFXPackageDiagramExporter = new JavaFXPackageDiagramExporter(graphSavePath, diagram);
        return javaFXPackageDiagramExporter.saveDiagram();
    }

    public Set<Vertex> loadDiagram(Path graphSavePath) {
        JavaFXPackageDiagramLoader javaFXPackageDiagramLoader = new JavaFXPackageDiagramLoader(graphSavePath);
        return javaFXPackageDiagramLoader.loadDiagram();
    }

    public Map<Vertex, Set<Arc<Vertex>>> convertCollectionsToDiagram() {
        GraphPackageDiagramConverter graphPackageDiagramConverter = new GraphPackageDiagramConverter(graphNodes.keySet());
        return graphPackageDiagramConverter.convertGraphToPackageDiagram();
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXPackageVisualization javaFXPackageVisualization = new JavaFXPackageVisualization(diagram);
        return javaFXPackageVisualization.createGraphView();
    }

    public File exportPlantUML(Path fileSavePth, PlantUMLExportType exportType) {
        PlantUMLPackageExporter plantUMLPackageExporter = new PlantUMLPackageExporter(fileSavePth, graphNodes, graphEdges);

        if (exportType.equals(PlantUMLExportType.TEXT)) {
            return plantUMLPackageExporter.exportPackageDiagramText();
        }else {
            return plantUMLPackageExporter.exportPackageDiagram();
        }
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
            Path path = Path.of(chosenPackage);
            if (!vertices.containsKey(path)) {
                continue;
            }
            chosenPackages.add(vertices.get(path));
        }
        return chosenPackages;
    }
}
