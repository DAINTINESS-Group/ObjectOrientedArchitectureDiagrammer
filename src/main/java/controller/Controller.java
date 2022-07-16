package controller;

import manager.diagram.ClassDiagramManager;
import manager.diagram.GraphMLDiagramManager;
import manager.diagram.PackageDiagramManager;
import model.PackageNode;
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

	public void exportDiagramToGraphML(String graphMLSavePath) {
		diagramManager.exportDiagramToGraphML(graphMLSavePath);
	}

	public abstract void convertTreeToDiagram(List<String> chosenPackagesNames);

}
