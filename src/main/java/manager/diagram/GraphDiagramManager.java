package manager.diagram;

import model.tree.SourceProject;

import java.io.File;
import java.util.*;
import java.util.List;

public abstract class GraphDiagramManager implements DiagramManager {

    private Diagram diagram;
    protected SourceProject sourceProject;

    public GraphDiagramManager(SourceProject sourceProject) {
        this.sourceProject = sourceProject;
    }
    public Map<String, Map<String, String>> createDiagram(List<String> chosenFilesNames){
        diagram = specifyDiagramType();
        return diagram.createDiagram(chosenFilesNames);
    }

    public Map<Integer, List<Double>> arrangeDiagram(){
        return diagram.arrangeDiagram();
    }

    public File exportDiagramToGraphML(String graphMLSavePath){
        return diagram.exportDiagramToGraphML(graphMLSavePath);
    }

    /**
     * This method is responsible for creating the corresponding type of Diagram, i.e., Class/Package Diagram
     * @return the Class/Package Diagram created
     */
    public abstract Diagram specifyDiagramType();

}