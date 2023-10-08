package controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import manager.SourceProject;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface Controller {

	/**
	 * This method creates the tree of the project by calling the DiagramManager's createTree method
	 * @param sourcePackagePath the project's source package path
	 * @return the SourceProject created
	 */
	SourceProject createTree(Path sourcePackagePath);

	/**
	 * This method converts the created tree to a diagram, by creating the corresponding DiagramManager
	 * based on the type of the graph, i.e. package or class and then calling the createDiagram method
	 * of the DiagramManager
	 * @param chosenFileNames the names of the files selected by the designer to be included in the diagram
	 */
	void convertTreeToDiagram(List<String> chosenFileNames);

	/**
	 * This method arranges the diagram by calling the DiagramManager's arrangeDiagram method
	 */
	void arrangeDiagram();

	// TODO JavaDoc
	SmartGraphPanel<String, String> applyLayout();

	// TODO JavaDoc
	SmartGraphPanel<String, String> applySpecificLayout(String choice);

	/**
	 * This method exports the diagram to a GraphML file by calling the DiagramManager's exportDiagramToGraphML method
	 * @param graphMLSavePath the selected path by the designer where the diagram will be saved
	 * @return the created File in which the diagram was saved
	 */
	File exportDiagramToGraphML(Path graphMLSavePath);

	/**
	 * This method saves the diagram to a text file by calling the DiagramManager's saveDiagram method
	 * @param graphSavePath the selected path by the designer where the diagram will be saved
	 * @return the created File in which the diagram was saved
	 */
	File saveDiagram(Path graphSavePath);

	/**
	 * This method loads a diagram from a text file by calling the DiagramManager's loadDiagram method
	 * @param graphSavePath the file's path where the diagram is saved
	 */
	void loadDiagram(Path graphSavePath);

	/**
	 * This method creates the JavaFX graphView by calling the DiagramManager's visualizeJavaFXGraph method
	 * @return the created graphView
	 */
	SmartGraphPanel<String, String> visualizeJavaFXGraph();

	/**
	 * This method creates the Loaded Diagram's JavaFX graphView by calling the DiagramManager's visualizeLoadedJavaFXGraph method
	 * @return the created graphView
	 */
	SmartGraphPanel<String, String> visualizeLoadedJavaFXGraph();

	/**
	 * This method exports the diagram as an image with the help of PlantUML by calling the DiagramManager's
	 * exportPlantUMLDiagram method
	 * @param graphSavePath the selected path by the designer where the diagram's image will be saved
	 * @return the created PlantUML diagram
	 */
	File exportPlantUMLDiagram(Path graphSavePath);

	/**
	 * This method saves the PlantUML code to a text file by calling the DiagramManager's exportPlantUMLText method
	 * @param textSavePath the selected path by the designer where the text file will be saved
	 * @return the created PlantUML text file
	 */
	File exportPlantUMLText(Path textSavePath);

}
