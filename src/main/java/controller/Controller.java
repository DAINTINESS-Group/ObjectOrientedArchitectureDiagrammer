package controller;

import manager.diagram.DiagramManager;
import model.PackageNode;
import parser.Parser;

import java.util.Map;

public class Controller {
	
	public Controller(String sourcePackagePath) {
		createTree(sourcePackagePath);
	}
	
	private void createTree(String sourcePackagePath) {
		Parser parser = new Parser(sourcePackagePath);
		createDiagram(parser.getPackageNodes());
	}

	private void createDiagram(Map<String, PackageNode> packageNodes) {
		new DiagramManager(packageNodes);
	}
}
