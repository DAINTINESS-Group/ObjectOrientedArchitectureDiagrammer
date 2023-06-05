package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLExportType;
import model.graph.Arc;
import model.graph.SinkVertex;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class ClassDiagramManager implements DiagramManager {

    private final SourceProject sourceProject;
    private final ArrayDeque<ClassDiagram> diagramStack;
    private Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;

    public ClassDiagramManager() {
        sourceProject = new SourceProject();
        diagramStack = new ArrayDeque<>();
    }

    public SourceProject createSourceProject(Path sourcePackagePath) {
        diagramStack.push(new ClassDiagram());
        sourceProject.createGraph(sourcePackagePath);
        Objects.requireNonNull(diagramStack.peek()).setSinkVertices(sourceProject.getSinkVertices());
        return sourceProject;
    }

    public void createDiagram(List<String> chosenFilesNames) {
        diagram = Objects.requireNonNull(diagramStack.peek()).createDiagram(chosenFilesNames);
    }

    public Map<Integer, List<Double>> arrangeDiagram(){
        return Objects.requireNonNull(diagramStack.peek()).arrangeDiagram();
    }

    public File exportDiagramToGraphML(Path graphMLSavePath) {
        return Objects.requireNonNull(diagramStack.peek()).exportDiagramToGraphML(graphMLSavePath);
    }

    public File exportPlantUML(Path fileSavePth, PlantUMLExportType exportType) {
        return Objects.requireNonNull(diagramStack.peek()).exportPlantUML(fileSavePth, exportType);
    }

    public File saveDiagram(Path graphSavePath) {
        return Objects.requireNonNull(diagramStack.peek()).saveDiagram(graphSavePath);
    }

    public void loadDiagram(Path graphSavePath) {
        diagramStack.push(new ClassDiagram());
        Objects.requireNonNull(diagramStack.peek()).loadDiagram(graphSavePath);
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        return Objects.requireNonNull(diagramStack.peek()).visualizeJavaFXGraph();
    }

    public ClassDiagram getDiagram() {
        return diagramStack.peek();
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> getCreatedDiagram() { return  diagram; }
}
