package controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

import model.diagram.javafx.JavaFXUMLNode;
import manager.DiagramManager;
import manager.SourceProject;

public interface Controller {

    /**
     * This method creates the tree of the project by calling {@link
     * DiagramManager#createSourceProject(Path)}.
     *
     * @param sourcePackagePath the project's source package path
     * @return the {@link SourceProject} created.
     */
    SourceProject createTree(Path sourcePackagePath);

    /**
     * This method converts the created tree to a diagram, by calling {@link
     * DiagramManager#convertTreeToDiagram(List)} with the given arguments.
     *
     * @param chosenFileNames the names of the files selected by the designer to be included in the
     *     diagram.
     */
    void convertTreeToDiagram(List<String> chosenFileNames);

    /** This method arranges the diagram by calling the {@link DiagramManager#arrangeDiagram()}. */
    void arrangeDiagram();

    // TODO JavaDoc
    SmartGraphPanel<JavaFXUMLNode, String> applyLayout();

    // TODO JavaDoc
    SmartGraphPanel<JavaFXUMLNode, String> applySpecificLayout(String choice);

    /**
     * This method exports the diagram to a GraphML file by calling {@link
     * DiagramManager#exportDiagramToGraphML(Path)}.
     *
     * @param graphMLSavePath the selected path by the designer where the diagram will be saved.
     * @return the created File in which the diagram was saved.
     */
    File exportDiagramToGraphML(Path graphMLSavePath);

    /**
     * This method saves the diagram to a text file by calling {@link
     * DiagramManager#saveDiagram(Path)}.
     *
     * @param graphSavePath the selected path by the designer where the diagram will be saved.
     * @return the created File in which the diagram was saved.
     */
    File saveDiagram(Path graphSavePath);

    /**
     * This method loads a diagram from a text file by calling {@link
     * DiagramManager#loadDiagram(Path)}.
     *
     * @param graphSavePath the file's path where the diagram is saved.
     */
    void loadDiagram(Path graphSavePath);

    /**
     * This method creates the JavaFX graphView by calling {@link
     * DiagramManager#visualizeJavaFXGraph()}.
     *
     * @return the created graphView {@link SmartGraphPanel}.
     */
    SmartGraphPanel<JavaFXUMLNode, String> visualizeJavaFXGraph();

    /**
     * // TODO: Write a Javadoc when this is done.
     *
     * @param dpi the dpi of the screen?
     * @return
     */
    String visualizeSvgGraph(int dpi);

    /**
     * This method creates the loaded Diagram's JavaFX graphView by calling {@link
     * DiagramManager#visualizeJavaFXGraph()}.
     *
     * @return the created {@link SmartGraphPanel}.
     */
    SmartGraphPanel<JavaFXUMLNode, String> visualizeLoadedJavaFXGraph();

    /**
     * This method exports the diagram as an image with the help of PlantUML by calling {@link
     * DiagramManager#exportPlantUMLImage(Path)}.
     *
     * @param graphSavePath the selected path by the designer where the diagram's image will be
     *     saved
     * @return the exported PlantUML diagram.
     */
    File exportPlantUMLDiagram(Path graphSavePath);

    /**
     * This method saves the PlantUML code to a text file by calling {@link
     * DiagramManager#exportPlantUMLText(Path)}}.
     *
     * @param textSavePath the selected path by the designer where the text file will be saved
     * @return the exported PlantUML text file.
     */
    File exportPlantUMLText(Path textSavePath);
}
