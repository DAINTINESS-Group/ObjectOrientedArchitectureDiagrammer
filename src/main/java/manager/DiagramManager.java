package manager;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

import model.diagram.javafx.JavaFXUMLNode;
import model.diagram.arrangement.geometry.DiagramGeometry;

public interface DiagramManager {

    /**
     * This method creates the tree of the project by parsing the project's source package that the
     * designer has loaded.
     *
     * @param sourcePackagePath the project's source package path
     * @return the {@link SourceProject} created, representing the model of the tree
     */
    SourceProject createSourceProject(Path sourcePackagePath);

    /**
     * This method converts the tree created by the Parser to a Diagram, based on the files(classes
     * or packages) selected by the designer. The type of the Diagram depends on the type of files
     * the designer has chosen and the Controller is responsible for creating the corresponding
     * GraphDiagramManager that implements the createDiagram method.
     *
     * @param chosenFilesNames the names of the files selected by the designer
     */
    void convertTreeToDiagram(List<String> chosenFilesNames);

    /**
     * This method arranges the createdDiagram's node geometry by creating a Jung Graph and then
     * applying the SpringLayout algorithm, implemented by the Jung library.
     */
    DiagramGeometry arrangeDiagram();

    /**
     * This method exports the created diagram to a file, to the path selected by the designer, in
     * GraphMLFormat, by converting the nodes and edges to GraphML syntax.
     *
     * @param graphMLSavePath the selected path by the designer where the diagram will be saved
     * @return the created File in which the diagram was saved
     */
    File exportDiagramToGraphML(Path graphMLSavePath);

    /**
     * This method saves the created JavaFX diagram to a text file, to the path selected by the
     * designer, by implementing a Map that stores the diagrams' nodes as keys and a Map of their
     * edges and the relationship type as their values.
     *
     * @param graphSavePath the selected path by the designer where the diagram will be saved
     * @return the created File in which the diagram was saved
     */
    File saveDiagram(Path graphSavePath);

    /**
     * This method loads a JavaFX diagram from a file, selected by the designer, by creating an
     * object of the class Diagram and populating the createdDiagram Collection with the contents of
     * the file. The createdDiagram is a Map as described in the method above.
     *
     * @param graphSavePath the file's path where the diagram is saved
     */
    void loadDiagram(Path graphSavePath);

    /**
     * This method creates the JavaFX graphView that will be rendered by view in the JavaFX Pane.
     *
     * @return the created graphView {@link SmartGraphPanel}
     */
    SmartGraphPanel<JavaFXUMLNode, String> visualizeJavaFXGraph();

    /**
     * TODO: Add Javadoc when this is done.
     *
     * @param dpi the screen's dpi?
     * @return
     */
    String visualizeSvgGraph(int dpi);

    /**
     * This method is responsible for exporting the diagram to a PlantUML image diagram.
     *
     * @param plantUMLSavePath the selected path by the designer where the exported diagram will be
     *     saved
     * @return the exported file
     */
    File exportPlantUMLImage(Path plantUMLSavePath);

    /**
     * This method is responsible for exporting the diagram to a PlantUML text file.
     *
     * @param textSavePath the selected path by the designer where the exported diagram will be
     *     saved
     * @return the exported file
     */
    File exportPlantUMLText(Path textSavePath);

    // TODO JavaDoc
    SmartGraphPanel<JavaFXUMLNode, String> applyLayout();

    // TODO JavaDoc
    SmartGraphPanel<JavaFXUMLNode, String> applySpecificLayout(String algorithmType);

    // TODO JavaDoc
    SmartGraphPanel<JavaFXUMLNode, String> visualizeLoadedJavaFXGraph();
}
