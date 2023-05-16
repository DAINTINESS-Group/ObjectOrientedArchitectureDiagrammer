package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.tree.SourceProject;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface Manager {

    /**This method creates the tree of the project, from the path to the source/package folder
     * selected by the designer
     * @param sourcePackagePath the project's source package path
     * @return the SourceProject created
     */
    SourceProject createTree(Path sourcePackagePath);

    /**
     * This method converts the tree created by the Parser to a Diagram, based on the files(classes or packages) selected
     * by the designer. The type of the Diagram depends on the type of files the designer has chosen and the Controller
     * is responsible for creating the corresponding GraphDiagramManager that implements the createDiagram method
     * @param chosenFilesNames the names of the files selected by the designer
     * @return the createDiagram
     */
    Map<String, Map<String, String>> createDiagram(List<String> chosenFilesNames);

    /**This method arranges the createdDiagram's node geometry by creating a Jung Graph and then applying the SpringLayout
     * algorithm, implemented by the Jung library
     * @return a Map with the nodes' ids as keys and their geometry as value
     */
    Map<Integer, List<Double>> arrangeDiagram();

    /**This method exports the created diagram to a file, to the path selected by the designer, in GraphMLFormat, by
     * converting the nodes and edges to GraphML syntax
     * @param graphMLSavePath the selected path by the designer where the diagram will be saved
     * @return the created File in which the diagram was saved
     */
    File exportDiagramToGraphML(Path graphMLSavePath);

    /**This method saves the created JavaFX diagram to a text file, to the path selected by the designer, by implementing
     * a Map that stores the diagrams' nodes as keys and a Map of their edges and the relationship type as their values
     * @param graphSavePath the selected path by the designer where the diagram will be saved
     * @return the created File in which the diagram was saved
     */
    File saveDiagram(Path graphSavePath);

    /**This method loads a JavaFX diagram from a file, selected by the designer, by creating an object of the class
     * Diagram and populating the createdDiagram Collection with the contents of the file. The createdDiagram is a Map
     * as described in the method above
     * @param graphSavePath the file's path where the diagram is saved
     * @return the createdDiagram
     */
    Map<String, Map<String, String>> loadDiagram(Path graphSavePath);

    /**This method creates the JavaFX's graphView that will be rendered by view in the JavaFX Pane
     * @return the created graphView
     */
    SmartGraphPanel<String, String> visualizeJavaFXGraph();

    /**This method exports the diagram as an image with the help of PlantUML by calling Diagram's 
     * exportPlantUMLDiagram method
     * @param graphSavePath the selected path by the designer where the diagram's image will be saved
     */
	void exportPlantUMLDiagram(Path graphSavePath);
	
    /**This method saves the PlantUML code to a text file by calling Diagram's
     * exportPlantUMLText method
     * @param textSavePath the selected path by the designer where the text file will be saved
     */
	void exportPlantUMLText(Path textSavePath);
	
	

}
