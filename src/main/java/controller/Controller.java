package controller;

import java.util.List;
import java.util.Map;

public interface Controller {

    /* This method creates the tree of the project, from the path to the source/package folder
     * selected by the designer */
    void createTree(String sourcePackagePath);

    /* This method converts the created tree to a diagram, by creating the corresponding GraphDiagramManager
     * based on the type of the graph, i.e. package or class and then calling the createDiagram method
     * of the GraphDiagramManager */
    void convertTreeToDiagram(List<String> chosenPackagesNames);

    /* This method arranges the diagram by calling the GraphDiagramManager's arrangeDiagram method */
    void arrangeDiagram();

    /* This method exports the diagram to a GraphML file by calling the GraphDiagramManager's exportDiagramToGraphML
     * method */
    void exportDiagramToGraphML(String graphMLSavePath);

    /* This method returns the graph that corresponds to the created diagram  by calling the GraphDiagramManager's
     * getDiagram method*/
    Map<String, Map<String, String>> getDiagram();
}
