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

    public SourceProject createTree(Path sourcePackagePath) {
        diagramStack.push(getDiagram());
        SourceProject srcPrj = Objects.requireNonNull(diagramStack.peek()).createTree(sourcePackagePath);
        
        //FIX: subpackage + factory
        Parser projectParser = new ProjectParser();
        PackageNode rootPackage = projectParser.parseSourcePackage(sourcePackagePath);
        Map<Path, PackageNode> packageNodes = projectParser.getPackageNodes();
        
        int resSize = srcPrj.setPackageNodes(packageNodes);
        return null;
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