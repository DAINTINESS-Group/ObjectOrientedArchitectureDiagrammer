package controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import manager.DiagramManager;
import model.diagram.plantuml.PlantUMLExportType;
import model.SourceProject;

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
	
	public File exportPlantUMLDiagram(Path graphSavePath) {
		return diagramManager.exportPlantUML(graphSavePath, PlantUMLExportType.DIAGRAM);
	}
	
	public File exportPlantUMLText(Path textSavePath) {
		return diagramManager.exportPlantUML(textSavePath, PlantUMLExportType.TEXT);
	}

	public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
		return diagramManager.visualizeJavaFXGraph();
	}

	public File saveDiagram(Path graphSavePath) {
		return diagramManager.saveDiagram(graphSavePath);
	}

	public Map<String, Map<String, String>> loadDiagram(Path graphSavePath) {
		return diagramManager.loadDiagram(graphSavePath);
	}

	public Map<String, Map<String, String>> convertTreeToDiagram(List<String> chosenClassesNames) {
		return diagramManager.createDiagram(chosenClassesNames);
	}

}
