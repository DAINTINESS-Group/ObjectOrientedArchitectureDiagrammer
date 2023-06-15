package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.DiagramExporter;
import model.diagram.PackageDiagram;
import model.graph.Arc;
import model.graph.Vertex;
import org.javatuples.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class PackageDiagramManager implements DiagramManager {

    private final ArrayDeque<PackageDiagram> diagramStack;
    private Map<Vertex, Set<Arc<Vertex>>> diagram;

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
        diagram = Objects.requireNonNull(diagramStack.peek()).createDiagram(chosenFilesNames);
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram(){
        return Objects.requireNonNull(diagramStack.peek()).arrangeDiagram();
    }

    @Override
    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        return Objects.requireNonNull(diagramStack.peek()).visualizeJavaFXGraph(diagram);
    }

    @Override
    public File exportDiagramToGraphML(Path graphMLSavePath) {
        DiagramExporter diagramExporter = Objects.requireNonNull(diagramStack.peek()).createGraphMLExporter();
        return diagramExporter.exportDiagram(graphMLSavePath);
    }

    @Override
    public File exportPlantUMLImage(Path plantUMLSavePath) {
        DiagramExporter diagramExporter =  Objects.requireNonNull(diagramStack.peek()).createPlantUMLImageExporter();
        return diagramExporter.exportDiagram(plantUMLSavePath);
    }

    @Override
    public File exportPlantUMLText(Path textSavePath) {
        DiagramExporter diagramExporter =  Objects.requireNonNull(diagramStack.peek()).createPlantUMLTextExporter();
        return diagramExporter.exportDiagram(textSavePath);
    }

    @Override
    public File saveDiagram(Path graphSavePath) {
        DiagramExporter diagramExporter =  Objects.requireNonNull(diagramStack.peek()).createJavaFXExporter(diagram);
        return diagramExporter.exportDiagram(graphSavePath);
    }

    @Override
    public void loadDiagram(Path graphSavePath) {
        diagramStack.push(new PackageDiagram());
        diagram = Objects.requireNonNull(diagramStack.peek()).loadDiagram(graphSavePath);
    }

    public PackageDiagram getPackageDiagram() {
        return diagramStack.peek();
    }

    public Map<Vertex, Set<Arc<Vertex>>> getDiagram() { return  diagram; }

}
