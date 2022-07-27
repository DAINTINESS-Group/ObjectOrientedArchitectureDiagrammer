package controller;

import manager.diagram.DiagramManager;
import model.tree.PackageNode;
import parser.Parser;
import parser.ProjectParser;

import java.util.List;
import java.util.Map;

public abstract class DiagramController implements Controller {

	protected Map<String, PackageNode> packageNodes;

	protected DiagramManager diagramManager;

	public void createTree(String sourcePackagePath) {
		Parser packageParser = new ProjectParser();
		packageParser.parseSourcePackage(sourcePackagePath);
		this.packageNodes = packageParser.getPackageNodes();
	}

	public void arrangeDiagram(){
		diagramManager.arrangeDiagram();
	}

	public void exportDiagramToGraphML(String graphMLSavePath) {
		diagramManager.exportDiagramToGraphML(graphMLSavePath);
	}

	public abstract void convertTreeToDiagram(List<String> chosenPackagesNames);

	public Map<String, Map<String, String>> getDiagram() {
		return diagramManager.getGraph();
	}

}
