package controller;

import model.tree.SourceProject;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface Controller {

    /**This method creates the tree of the project, from the path to the source/package folder
     * selected by the designer
     * @param sourcePackagePath the project's source package path
     * @return the SourceProject created
     */
    SourceProject createTree(String sourcePackagePath);

    /**
     * This method converts the created tree to a diagram, by creating the corresponding GraphDiagramManager
     * based on the type of the graph, i.e. package or class and then calling the createDiagram method
     * of the GraphDiagramManager
     *
     * @param chosenFileNames the names of the files selected by the designer to be included in the diagram
     * @return the diagram created
     */
    Map<String, Map<String, String>> convertTreeToDiagram(List<String> chosenFileNames);

    /**This method arranges the diagram by calling the GraphDiagramManager's arrangeDiagram method
     * @return a Map with the nodes' ids as keys and their geometry as value
     */
    Map<Integer, List<Double>> arrangeDiagram();

    /**This method exports the diagram to a GraphML file by calling the GraphDiagramManager's exportDiagramToGraphML
     * method
     * @param graphMLSavePath the selected path by the designer where the diagram will be saved
     * @return the created File in which the diagram was saved
     */
    File exportDiagramToGraphML(String graphMLSavePath);

}
