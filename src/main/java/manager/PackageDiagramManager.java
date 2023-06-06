package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.PackageDiagram;
import model.diagram.plantuml.PlantUMLExportType;
import model.graph.Arc;
import model.graph.Vertex;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class PackageDiagramManager implements DiagramManager {

    private final ArrayDeque<PackageDiagram> diagramStack;
    private Map<Vertex, Set<Arc<Vertex>>> diagram;

    public PackageDiagramManager() {
        diagramStack = new ArrayDeque<>();
    }

    public SourceProject createSourceProject(Path sourcePackagePath) {
        SourceProject sourceProject = new SourceProject();
        sourceProject.createGraph(sourcePackagePath);
        diagramStack.push(new PackageDiagram());
        Objects.requireNonNull(diagramStack.peek()).setVertices(sourceProject.getVertices());
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
        diagramStack.push(new PackageDiagram());
        diagram = Objects.requireNonNull(diagramStack.peek()).loadDiagram(graphSavePath);
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        return Objects.requireNonNull(diagramStack.peek()).visualizeJavaFXGraph();
    }

    public PackageDiagram getDiagram() {
        return diagramStack.peek();
    }

    public Map<Vertex, Set<Arc<Vertex>>> getCreatedDiagram() { return  diagram; }

}
