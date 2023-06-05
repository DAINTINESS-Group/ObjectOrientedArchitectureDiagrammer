package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.plantuml.PlantUMLExportType;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface DiagramManager {

    /**This method creates the tree of the project by parsing the project's source package that the designer has loaded
     * @param sourcePackagePath the project's source package path
     * @return the SourceProject created, representing the model of the tree
     */
    SourceProject createSourceProject(Path sourcePackagePath);

    /**
     * This method converts the tree created by the Parser to a Diagram, based on the files(classes or packages) selected
     * by the designer. The type of the Diagram depends on the type of files the designer has chosen and the Controller
     * is responsible for creating the corresponding GraphDiagramManager that implements the createDiagram method
     *
     * @param chosenFilesNames the names of the files selected by the designer
     * @return the createDiagram
     */
    void createDiagram(List<String> chosenFilesNames);

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
    void loadDiagram(Path graphSavePath);

    /**This method creates the JavaFX's graphView that will be rendered by view in the JavaFX Pane
     * @return the created graphView
     */
    SmartGraphPanel<String, String> visualizeJavaFXGraph();

    /**
     * This method is responsible for exporting the diagram to PlantUML based on the export type given, i.e. text & diagram
     *
     * @param fileSavePth the selected path by the designer where the exported diagram will be saved
     * @param exportType  the type of the exportation
     * @return the exported file
     */
    File exportPlantUML(Path fileSavePth, PlantUMLExportType exportType);

}
