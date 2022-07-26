package controller;

import manager.diagram.GraphMLDiagramManager;
import model.tree.PackageNode;
import parser.PackageParser;
import parser.Parser;

import java.util.List;
import java.util.Map;

public abstract class Controller implements DiagramController {

	protected Map<String, PackageNode> packageNodes;

	protected GraphMLDiagramManager diagramManager;

	public void createTree(String sourcePackagePath) {
		PackageParser packageParser = new Parser();
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
