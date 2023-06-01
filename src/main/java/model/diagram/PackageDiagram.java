package model.diagram;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.SourceProject;
import model.diagram.graphml.GraphMLPackageExporter;
import model.diagram.javafx.JavaFXExporter;
import model.diagram.javafx.JavaFXLoader;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.plantuml.PlantUMLExportType;
import model.diagram.plantuml.PlantUMLPackageExporter;
import model.graph.Arc;
import model.graph.Vertex;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageDiagram {

    private Map<Integer, List<Double>> nodesGeometry;
    private SourceProject sourceProject;
    private Map<String, Map<String, String>> createdDiagram;
    private final Map<Vertex, Integer> graphNodes;
    private final Map<Arc<Vertex>, Integer> graphEdges;

    public PackageDiagram() {
        createdDiagram = new HashMap<>();
        graphNodes = new HashMap<>();
        graphEdges = new HashMap<>();
    }

    public Map<String, Map<String, String>> createDiagram(List<String> chosenFileNames) {
        createNodeCollection(chosenFileNames);
        createEdgeCollection();
        createdDiagram = convertCollectionsToDiagram();
        return createdDiagram;
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
        JavaFXExporter javaFXExporter = new JavaFXExporter();
        return javaFXExporter.saveDiagram(createdDiagram, graphSavePath);
    }

    public Map<String, Map<String, String>> loadDiagram(Path graphSavePath) {
        JavaFXLoader javaFXLoader = new JavaFXLoader();
        createdDiagram = javaFXLoader.loadDiagram(graphSavePath);
        return createdDiagram;
    }

    public Map<String, Map<String, String>> convertCollectionsToDiagram() {
        CollectionsDiagramConverter collectionsDiagramConverter = new CollectionsDiagramConverter();
        return collectionsDiagramConverter.convertPackageCollectionsToDiagram(graphNodes, graphEdges);
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXVisualization javaFXVisualization = new JavaFXVisualization();
        return javaFXVisualization.createGraphView(createdDiagram);
    }

    public File exportPlantUML(Path fileSavePth, PlantUMLExportType exportType) {
        PlantUMLPackageExporter plantUMLPackageExporter = new PlantUMLPackageExporter(fileSavePth, graphNodes, graphEdges);

        if (exportType.equals(PlantUMLExportType.TEXT)) {
            return plantUMLPackageExporter.exportPackageDiagramText();
        }else {
            return plantUMLPackageExporter.exportPackageDiagram();
        }
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

    public void setSourceProject(SourceProject sourceProject) {
        this.sourceProject = sourceProject;
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
            if (!sourceProject.getVertices().containsKey(Paths.get(chosenPackage))) {
                continue;
            }
            chosenPackages.add(sourceProject.getVertices().get(Paths.get(chosenPackage)));
        }
        return chosenPackages;
    }

}
