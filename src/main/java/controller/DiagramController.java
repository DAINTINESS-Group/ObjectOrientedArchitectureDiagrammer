package controller;

import manager.diagram.Manager;
import model.tree.SourceProject;

import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class DiagramController implements Controller {

	protected Manager diagramManager;

	public SourceProject createTree(String sourcePackagePath) {
		return diagramManager.createTree(sourcePackagePath);
	}

	public Map<Integer, List<Double>> arrangeDiagram(){
		return diagramManager.arrangeDiagram();
	}

	public File exportDiagramToGraphML(String graphMLSavePath) {
		return diagramManager.exportDiagramToGraphML(graphMLSavePath);
	}

	public File saveDiagram(String graphSavePath) {
		return diagramManager.saveDiagram(graphSavePath);
	}

	public abstract Map<String, Map<String, String>> loadDiagram(String graphSavePath);

	public abstract Map<String, Map<String, String>> convertTreeToDiagram(List<String> chosenFileNames);

}
