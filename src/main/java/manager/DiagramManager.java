package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.Diagram;
import model.tree.PackageNode;
import model.tree.SourceProject;
import parser.Parser;
import parser.ProjectParser;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public abstract class DiagramManager implements Manager {

    private final ArrayDeque<Diagram> diagramStack;

    public DiagramManager() {
        diagramStack = new ArrayDeque<>();
    }

    /**
     * This method is responsible for creating the tree of the source package
     * It parses the source package using the Parser and populates the nodes of the sourceProject
     * @param sourcePackagePath the project's source package path
     * @return the sourceProject representing the model created by parsing the source package
     */
    public SourceProject createTree(Path sourcePackagePath) {
        diagramStack.push(getDiagram());
        SourceProject sourceProject = Objects.requireNonNull(diagramStack.peek()).createSourceProject(sourcePackagePath);
        
        Parser projectParser = new ProjectParser();
        projectParser.parseSourcePackage(sourcePackagePath);
        Map<Path, PackageNode> packageNodes = projectParser.getPackageNodes();
        sourceProject.setPackageNodes(packageNodes);
        sourceProject.setProjectsProperties();

        return sourceProject;
    }

    public Map<String, Map<String, String>> createDiagram(List<String> chosenFilesNames) {
        return Objects.requireNonNull(diagramStack.peek()).createDiagram(chosenFilesNames);
    }

    public Map<Integer, List<Double>> arrangeDiagram(){
        return Objects.requireNonNull(diagramStack.peek()).arrangeDiagram();
    }

    public File exportDiagramToGraphML(Path graphMLSavePath) {
        return Objects.requireNonNull(diagramStack.peek()).exportDiagramToGraphML(graphMLSavePath);
    }
    
    public void exportPlantUMLDiagram(Path selectedFile) {
    	Objects.requireNonNull(diagramStack.peek()).exportPlantUMLDiagram(selectedFile);
    }

    public File saveDiagram(Path graphSavePath) {
        return Objects.requireNonNull(diagramStack.peek()).saveDiagram(graphSavePath);
    }

    public Map<String, Map<String, String>> loadDiagram(Path graphSavePath) {
        diagramStack.push(getDiagram());
        return Objects.requireNonNull(diagramStack.peek()).loadDiagram(graphSavePath);
    }

    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        return Objects.requireNonNull(diagramStack.peek()).visualizeJavaFXGraph();
    }

    /**
     * This method is responsible for creating the corresponding type of Diagram, i.e., Class/Package Diagram
     * @return the Class/Package Diagram created
     */
    public abstract Diagram getDiagram();

}