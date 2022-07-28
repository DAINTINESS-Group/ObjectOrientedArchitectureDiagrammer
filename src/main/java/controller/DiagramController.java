package controller;

import manager.diagram.DiagramManager;
import model.tree.SourceProject;

import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class DiagramController implements Controller {

	protected DiagramManager diagramManager;
	protected SourceProject sourceProject;

	public SourceProject createTree(String sourcePackagePath) {
		sourceProject = new SourceProject(sourcePackagePath);
		sourceProject.parseSourceProject();
		return sourceProject;
	}

	public Map<Integer, List<Double>> arrangeDiagram(){
		return diagramManager.arrangeDiagram();
	}

	public File exportDiagramToGraphML(String graphMLSavePath) {
		return diagramManager.exportDiagramToGraphML(graphMLSavePath);
	}

	public abstract Map<String, Map<String, String>> convertTreeToDiagram(List<String> chosenFileNames);

}
