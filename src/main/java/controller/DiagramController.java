package controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import manager.DiagramManager;
import manager.SourceProject;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class DiagramController implements Controller {

	protected DiagramManager diagramManager;

	public SourceProject createTree(Path sourcePackagePath) {
		return diagramManager.createSourceProject(sourcePackagePath);
	}

	public Map<Integer, List<Double>> arrangeDiagram(){
		return diagramManager.arrangeDiagram();
	}

	public File exportDiagramToGraphML(Path graphMLSavePath) {
		return diagramManager.exportDiagramToGraphML(graphMLSavePath);
	}
	
	public File exportPlantUMLDiagram(Path plantUMLSavePath) {
		return diagramManager.exportPlantUMLDiagram(plantUMLSavePath);
	}
	
	public File exportPlantUMLText(Path textSavePath) {
		return diagramManager.exportPlantUMLText(textSavePath);
	}

	public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
		return diagramManager.visualizeJavaFXGraph();
	}

	public File saveDiagram(Path graphSavePath) {
		return diagramManager.saveDiagram(graphSavePath);
	}

	public void loadDiagram(Path graphSavePath) {
		diagramManager.loadDiagram(graphSavePath);
	}

	public void convertTreeToDiagram(List<String> chosenClassesNames) {
		diagramManager.createDiagram(chosenClassesNames);
	}

}
