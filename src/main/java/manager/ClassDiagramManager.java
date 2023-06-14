package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.ClassDiagram;
import model.diagram.DiagramExporter;
import model.diagram.ShadowCleaner;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.javatuples.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class ClassDiagramManager implements DiagramManager {

    private final ArrayDeque<ClassDiagram> diagramStack;
    private Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;

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
        diagram = Objects.requireNonNull(diagramStack.peek()).createDiagram(chosenFilesNames);
        ShadowCleaner shadowCleaner = new ShadowCleaner(diagram);
        diagram = shadowCleaner.shadowWeakRelationships();
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
    public File exportPlantUMLDiagram(Path plantUMLSavePath) {
        DiagramExporter diagramExporter = Objects.requireNonNull(diagramStack.peek()).createPlantUMLImageExporter();
        return diagramExporter.exportDiagram(plantUMLSavePath);
    }

    @Override
    public File exportPlantUMLText(Path textSavePath) {
        DiagramExporter diagramExporter = Objects.requireNonNull(diagramStack.peek()).createPlantUMLTextExporter();
        return diagramExporter.exportDiagram(textSavePath);
    }

    @Override
    public File saveDiagram(Path graphSavePath) {
        DiagramExporter diagramExporter = Objects.requireNonNull(diagramStack.peek()).createJavaFXExporter(diagram);
        return diagramExporter.exportDiagram(graphSavePath);
    }

    @Override
    public void loadDiagram(Path graphSavePath) {
        diagramStack.push(new ClassDiagram());
        diagram = Objects.requireNonNull(diagramStack.peek()).loadDiagram(graphSavePath);
        ShadowCleaner shadowCleaner = new ShadowCleaner(diagram);
        diagram = shadowCleaner.shadowWeakRelationships();
    }

    public ClassDiagram getClassDiagram() {
        return diagramStack.peek();
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> getDiagram() { return  diagram; }

}
