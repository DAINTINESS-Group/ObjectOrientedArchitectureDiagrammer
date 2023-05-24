package model.diagram.plantuml;

import model.tree.node.LeafNode;
import model.tree.node.ModifierType;
import model.tree.node.Node;

import java.util.List;

public class PlantUMLLeafNode {

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
    	StringBuilder plantUMLFields = new StringBuilder();
    	for (int i=0; i<fieldNames.size();i++) {
			plantUMLFields.append(visibilityToPlantUML(fieldVisibilitiesList.get(i))).append(fieldNames.get(i)).append(": ")
					.append(fieldTypes.get(i)).append("\n");
		}
		return plantUMLFields.toString();
	}
    
    private String convertMethodsToPlantUML(Node node) {
		List<String> methodNames = ((LeafNode) node).getMethodNamesList();
		List<String> methodReturnTypes = ((LeafNode) node).getMethodReturnTypesList();
		List<ModifierType> methodVisibilitiesList = ((LeafNode) node).getMethodVisibilitiesList();
		List<StringBuilder> methodParameters = ((LeafNode) node).getMethodParametersList();
		StringBuilder plantUMLMethods = new StringBuilder();
		for (int i=0; i<methodNames.size();i++) {
			plantUMLMethods.append(visibilityToPlantUML(methodVisibilitiesList.get(i))).append(methodNames.get(i)).append("(")
					.append(methodParameters.get(i)).append("): ").append(methodReturnTypes.get(i)).append("\n");
		}
    	return plantUMLMethods.toString();
    }
    
    private String visibilityToPlantUML(ModifierType visibilityEnum) {
		return switch (visibilityEnum) {
			case PRIVATE -> "-";
			case PUBLIC -> "+";
			case PROTECTED -> "#";
			default -> "~";
		};
    }

}
