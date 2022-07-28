package manager.diagram;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface DiagramManager {

    /**
     * This method converts the tree created by the Parser to a diagram, based on the files(classes or packages) selected
     * by the designer. The type of the diagram depends on the type of files the designer has chosen and the controller
     * is responsible for creating the corresponding GraphDiagramManager that implements the createDiagram method
     *
     * @param chosenFilesNames the names of the files selected by the designer
     * @return the created diagram
     */
    Map<String, Map<String, String>> createDiagram(List<String> chosenFilesNames);

    /**This method arranges the created diagram's node geometry by creating a Jung Graph and then applying the SpringLayout
     * algorithm, implemented by the Jung library
     * @return a Map with the nodes' ids as keys and their geometry as value
     */
    Map<Integer, List<Double>> arrangeDiagram();

    /**This method exports the created diagram to a file, to the path selected by the designer, in GraphMLFormat, by
     * converting the nodes and edges to GraphML syntax
     * @param graphMLSavePath the selected path by the designer where the diagram will be saved
     * @return the created File in which the diagram was saved
     */
    File exportDiagramToGraphML(String graphMLSavePath);

}
