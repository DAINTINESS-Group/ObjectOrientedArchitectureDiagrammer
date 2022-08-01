package manager.diagram;

import model.diagram.Diagram;
import model.tree.SourceProject;

import java.io.File;
import java.util.*;
import java.util.List;

public abstract class DiagramManager implements Manager {

    private final ArrayDeque<Diagram> diagramStack;
    private Diagram diagram;

    public DiagramManager() {
        diagramStack = new ArrayDeque<>();
    }

    public SourceProject createTree(String sourcePackagePath) {
        diagram = getDiagramType();
        diagramStack.push(diagram);
        return diagram.createTree(sourcePackagePath);
    }

    public Map<String, Map<String, String>> createDiagram(List<String> chosenFilesNames) {
        return diagram.createDiagram(chosenFilesNames);
    }

    public Map<Integer, List<Double>> arrangeDiagram(){
        return diagram.arrangeDiagram();
    }

    public File exportDiagramToGraphML(String graphMLSavePath) {
        return diagram.exportDiagramToGraphML(graphMLSavePath);
    }

    public File saveDiagram(String graphSavePath) {
        return diagram.saveDiagram(graphSavePath);
    }

    public Map<String, Map<String, String>> loadDiagram(String graphSavePath) {
        diagram = getDiagramType();
        diagramStack.push(diagram);
        return diagram.loadDiagram(graphSavePath);
    }

    /**
     * This method is responsible for creating the corresponding type of Diagram, i.e., Class/Package Diagram
     * @return the Class/Package Diagram created
     */
    public abstract Diagram getDiagramType();

}