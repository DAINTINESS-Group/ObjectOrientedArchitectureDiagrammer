package controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.tree.SourceProject;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface Controller {

    /**This method creates the tree of the project by calling the DiagramManager's createTree method
     * @param sourcePackagePath the project's source package path
     * @return the SourceProject created
     */
    SourceProject createTree(Path sourcePackagePath);

    /**
     * This method converts the created tree to a diagram, by creating the corresponding DiagramManager
     * based on the type of the graph, i.e. package or class and then calling the createDiagram method
     * of the DiagramManager
     *
     * @param chosenFileNames the names of the files selected by the designer to be included in the diagram
     * @return the diagram created
     */
    Map<String, Map<String, String>> convertTreeToDiagram(List<String> chosenFileNames);

    /**This method arranges the diagram by calling the DiagramManager's arrangeDiagram method
     * @return a Map with the nodes' ids as keys and their geometry as value
     */
    Map<Integer, List<Double>> arrangeDiagram();

    /**This method exports the diagram to a GraphML file by calling the DiagramManager's exportDiagramToGraphML
     * method
     * @param graphMLSavePath the selected path by the designer where the diagram will be saved
     * @return the created File in which the diagram was saved
     */
    File exportDiagramToGraphML(Path graphMLSavePath);

    /**This method saves the diagram to a text file by calling the DiagramManager's saveDiagram method
     * @param graphSavePath the selected path by the designer where the diagram will be saved
     * @return the created File in which the diagram was saved
     */
    File saveDiagram(Path graphSavePath);

    /**This method loads a diagram from a text file by calling the DiagramManager's loadDiagram method
     * @param graphSavePath the file's path where the diagram is saved
     * @return the created diagram
     */
    Map<String, Map<String, String>> loadDiagram(Path graphSavePath);

    /**This method creates the JavaFX's graphView by calling the DiagramManager's visualizeJavaFXGraph method
     * @return the created graphView
     */
    SmartGraphPanel<String, String> visualizeJavaFXGraph();

}
