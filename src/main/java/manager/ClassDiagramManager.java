package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.ClassDiagram;
import model.diagram.GraphClassDiagramConverter;
import model.diagram.arrangement.ClassDiagramArrangement;
import model.diagram.arrangement.DiagramArrangement;
import model.diagram.exportation.*;
import model.diagram.ShadowCleaner;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.javafx.classdiagram.JavaFXClassDiagramLoader;
import model.diagram.javafx.classdiagram.JavaFXClassVisualization;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.javatuples.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class ClassDiagramManager implements DiagramManager {

    private final ArrayDeque<ClassDiagram> diagramStack;
    private Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;
    private Map<Integer, Pair<Double, Double>> diagramGeometry;
    private Map<SinkVertex, Integer> graphNodes;

    public ClassDiagramManager() {
        diagramStack = new ArrayDeque<>();
    }

    @Override
    public SourceProject createSourceProject(Path sourcePackagePath) {
        SourceProject sourceProject = new SourceProject();
        sourceProject.createGraph(sourcePackagePath);
        diagramStack.push(new ClassDiagram());
        Objects.requireNonNull(diagramStack.peek()).setSinkVertices(sourceProject.getSinkVertices());
        return sourceProject;
    }

    @Override
    public void convertTreeToDiagram(List<String> chosenFilesNames) {
        graphNodes = Objects.requireNonNull(diagramStack.peek()).createGraphNodes(chosenFilesNames);
        GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
        diagram = graphClassDiagramConverter.convertGraphToClassDiagram();
        ShadowCleaner shadowCleaner = new ShadowCleaner(diagram);
        diagram = shadowCleaner.shadowWeakRelationships();
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram(){
        DiagramArrangement classDiagramArrangement = new ClassDiagramArrangement(graphNodes, diagram);
        diagramGeometry = classDiagramArrangement.arrangeDiagram();
        return diagramGeometry;
    }

    @Override
    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXVisualization javaFXVisualization = new JavaFXClassVisualization(diagram);
        return javaFXVisualization.createGraphView();
    }

    @Override
    public File exportDiagramToGraphML(Path graphMLSavePath) {
        DiagramExporter diagramExporter = new GraphMLClassDiagramExporter(graphNodes, diagramGeometry, diagram);
        return diagramExporter.exportDiagram(graphMLSavePath);
    }

    @Override
    public File exportPlantUMLImage(Path plantUMLSavePath) {
        DiagramExporter diagramExporter = new PlantUMLClassDiagramImageExporter(diagram);
        return diagramExporter.exportDiagram(plantUMLSavePath);
    }

    @Override
    public File exportPlantUMLText(Path textSavePath) {
        DiagramExporter diagramExporter = new PlantUMLClassDiagramTextExporter(diagram);
        return diagramExporter.exportDiagram(textSavePath);
    }

    @Override
    public File saveDiagram(Path graphSavePath) {
        DiagramExporter diagramExporter = new JavaFXClassDiagramExporter(diagram);
        return diagramExporter.exportDiagram(graphSavePath);
    }

    @Override
    public void loadDiagram(Path graphSavePath) {
        diagramStack.push(new ClassDiagram());
        JavaFXClassDiagramLoader javaFXClassDiagramLoader =  new JavaFXClassDiagramLoader(graphSavePath);
        Set<SinkVertex> loadedDiagram = javaFXClassDiagramLoader.loadDiagram();
        GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(loadedDiagram);
        diagram = graphClassDiagramConverter.convertGraphToClassDiagram();
        ShadowCleaner shadowCleaner = new ShadowCleaner(diagram);
        diagram = shadowCleaner.shadowWeakRelationships();
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> getDiagram() { return  diagram; }

    public Map<SinkVertex, Integer> getGraphNodes() {
        return graphNodes;
    }

}
