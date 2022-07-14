package controller;

import manager.diagram.ClassDiagramManager;
import manager.diagram.GraphMLDiagramManager;
import manager.diagram.PackageDiagramManager;
import model.PackageNode;
import parser.PackageParser;
import parser.Parser;

import java.util.List;
import java.util.Map;

public class Controller implements DiagramController {

	private Map<String, PackageNode> packageNodes;

	private GraphMLDiagramManager diagramManager;

	public void createTree(String sourcePackagePath) {
		PackageParser packageParser = new Parser();
		packageParser.parseSourcePackage(sourcePackagePath);
		this.packageNodes = packageParser.getPackageNodes();
	}

	public void convertTreeToPackageDiagram(List<String> chosenPackagesNames) {
		diagramManager = new PackageDiagramManager(packageNodes);
		diagramManager.createDiagram(chosenPackagesNames);
	}

	public void convertTreeToClassDiagram(List<String> chosenClassesNames) {
		diagramManager = new ClassDiagramManager(packageNodes);
		diagramManager.createDiagram(chosenClassesNames);
	}

	public void exportDiagramToGraphML(String graphMLSavePath) {
		diagramManager.exportDiagramToGraphML(graphMLSavePath);
	}


}
