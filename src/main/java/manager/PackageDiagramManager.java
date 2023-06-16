package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.GraphPackageDiagramConverter;
import model.diagram.arrangement.DiagramArrangement;
import model.diagram.arrangement.PackageDiagramArrangement;
import model.diagram.exportation.*;
import model.diagram.PackageDiagram;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.javafx.packagediagram.JavaFXPackageDiagramLoader;
import model.diagram.javafx.packagediagram.JavaFXPackageVisualization;
import model.graph.Arc;
import model.graph.Vertex;
import org.javatuples.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class PackageDiagramManager implements DiagramManager {

    private final ArrayDeque<PackageDiagram> diagramStack;
    private Map<Vertex, Set<Arc<Vertex>>> diagram;
    private Map<Integer, Pair<Double, Double>> diagramGeometry;
    private Map<Vertex, Integer> graphNodes;

    public PackageDiagramManager() {
        diagramStack = new ArrayDeque<>();
    }

    @Override
    public SourceProject createSourceProject(Path sourcePackagePath) {
        SourceProject sourceProject = new SourceProject();
        sourceProject.createGraph(sourcePackagePath);
        diagramStack.push(new PackageDiagram());
        Objects.requireNonNull(diagramStack.peek()).setVertices(sourceProject.getVertices());
        return sourceProject;
    }

    @Override
    public void convertTreeToDiagram(List<String> chosenFilesNames) {
        graphNodes = Objects.requireNonNull(diagramStack.peek()).createGraphNodes(chosenFilesNames);
        GraphPackageDiagramConverter graphPackageDiagramConverter = new GraphPackageDiagramConverter(graphNodes.keySet());
        diagram =  graphPackageDiagramConverter.convertGraphToPackageDiagram();
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram(){
        DiagramArrangement diagramArrangement = new PackageDiagramArrangement(graphNodes, diagram);
        diagramGeometry = diagramArrangement.arrangeDiagram();
        return diagramGeometry;
    }

    @Override
    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXVisualization javaFXPackageVisualization = new JavaFXPackageVisualization(diagram);
        return javaFXPackageVisualization.createGraphView();
    }

    @Override
    public void loadDiagram(Path graphSavePath) {
        diagramStack.push(new PackageDiagram());
        JavaFXPackageDiagramLoader javaFXPackageDiagramLoader = new JavaFXPackageDiagramLoader(graphSavePath);
        Set<Vertex> loadedDiagram = javaFXPackageDiagramLoader.loadDiagram();
        GraphPackageDiagramConverter graphPackageDiagramConverter = new GraphPackageDiagramConverter(loadedDiagram);
        diagram = graphPackageDiagramConverter.convertGraphToPackageDiagram();
    }

    @Override
    public File exportDiagramToGraphML(Path graphMLSavePath) {
        DiagramExporter diagramExporter = new GraphMLPackageDiagramExporter(graphNodes, diagramGeometry, diagram);
        return diagramExporter.exportDiagram(graphMLSavePath);
    }

    @Override
    public File exportPlantUMLImage(Path plantUMLSavePath) {
        DiagramExporter diagramExporter =  new PlantUMLPackageDiagramImageExporter(diagram);
        return diagramExporter.exportDiagram(plantUMLSavePath);
    }

    @Override
    public File exportPlantUMLText(Path textSavePath) {
        DiagramExporter diagramExporter =  new PlantUMLPackageDiagramTextExporter(diagram);
        return diagramExporter.exportDiagram(textSavePath);
    }

    @Override
    public File saveDiagram(Path graphSavePath) {
        DiagramExporter diagramExporter =  new JavaFXPackageDiagramExporter(diagram);
        return diagramExporter.exportDiagram(graphSavePath);
    }

    public Map<Vertex, Set<Arc<Vertex>>> getDiagram() { return  diagram; }

    public Map<Vertex, Integer> getGraphNodes() {
        return graphNodes;
    }

}
