package model.diagram.plantuml;

import java.util.List;

import model.diagram.GraphNodeCollection;
import model.tree.node.LeafNode;
import model.tree.node.ModifierType;
import model.tree.node.Node;

public class PlantUMLLeafNode extends GraphNodeCollection{

	public String convertPlantNode(Node node) {
		String plantUMLDeclaration = node.getType().toString().toLowerCase() + " " + node.getName() + " {\n";
		plantUMLDeclaration += convertFieldsToPlantUML(node);
		plantUMLDeclaration += convertMethodsToPlantUML(node) + "}\n";
		return plantUMLDeclaration;
	}
	
    private String convertFieldsToPlantUML(Node node) {
    	List<String> fieldNames = ((LeafNode) node).getFieldNamesList();
    	List<String> fieldTypes = ((LeafNode) node).getFieldTypesList();
    	List<ModifierType> fieldVisibilitiesList = ((LeafNode) node).getFieldVisibilitiesList();
    	String plantUMLFields = "";
    	for (int i=0; i<fieldNames.size();i++) {
			plantUMLFields += visibilityToPlantUML(fieldVisibilitiesList.get(i)) + fieldNames.get(i) + ": " + fieldTypes.get(i) + "\n";
		}
		return plantUMLFields;
	}
    
    private String convertMethodsToPlantUML(Node node) {
		List<String> methodNames = ((LeafNode) node).getMethodNamesList();
		List<String> methodReturnTypes = ((LeafNode) node).getMethodReturnTypesList();
		List<ModifierType> methodVisibilitiesList = ((LeafNode) node).getMethodVisibilitiesList();
		List<StringBuilder> methodParameters = ((LeafNode) node).getMethodParametersList();
		String plantUMLMethods = "";
		for (int i=0; i<methodNames.size();i++) {
			plantUMLMethods += visibilityToPlantUML(methodVisibilitiesList.get(i)) + methodNames.get(i) + "(" + methodParameters.get(i) + "): " + methodReturnTypes.get(i) + "\n";
		}
    	return plantUMLMethods;
    }
    
    private String visibilityToPlantUML(ModifierType visibilityEnum) {
		return switch (visibilityEnum) {
			case PRIVATE -> "-";
			case PUBLIC -> "+";
			case PROTECTED -> "#";
			default -> "~";
		};
    }

	public String convertNode(Node node, int nodeId, List<Double> nodesGeometry) {
		// Do nothing
		return null;
	}
	
}
