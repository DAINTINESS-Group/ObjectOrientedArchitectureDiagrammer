package model.diagram.plantuml;

import model.tree.node.Node;

public class PlantUMLPackageNode {

	public String convertPlantNode(Node node) {
		return node.getType().toString().toLowerCase() + " " + node.getName() + " {\n" + "}\n";
	}
	
}
