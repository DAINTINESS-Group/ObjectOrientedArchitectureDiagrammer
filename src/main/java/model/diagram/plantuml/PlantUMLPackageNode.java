package model.diagram.plantuml;

import java.util.List;

import model.diagram.GraphNodeCollection;
import model.tree.node.Node;

public class PlantUMLPackageNode extends GraphNodeCollection{

	public String convertPlantNode(Node node) {
		String plantUMLDeclaration = node.getType().toString().toLowerCase() + " " + node.getName() + " {\n" + "}\n";
		return plantUMLDeclaration;
	}
	
	public String convertNode(Node node, int nodeId, List<Double> nodesGeometry) {
		// Do nothing
		return null;
	}

}
