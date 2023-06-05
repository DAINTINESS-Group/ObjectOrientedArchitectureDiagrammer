package model.diagram.plantuml;

import model.graph.ModifierType;
import model.graph.SinkVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlantUMLSinkVertex {

	private final Map<SinkVertex, Integer> graphNodes;

	public PlantUMLSinkVertex(Map<SinkVertex, Integer> graphNodes) {
		this.graphNodes = graphNodes;
	}

	public StringBuilder convertPlantLeafNode() {
		StringBuilder plantUMLBuffer = new StringBuilder();
		for (SinkVertex node : graphNodes.keySet()) {
			String plantUMLDeclaration = node.getVertexType().toString().toLowerCase() + " " + node.getName() + " {\n";
			plantUMLDeclaration += convertFieldsToPlantUML(node);
			plantUMLDeclaration += convertMethodsToPlantUML(node) + "}\n";
			plantUMLBuffer.append(plantUMLDeclaration);
		}
		return plantUMLBuffer;
	}
	
    private String convertFieldsToPlantUML(SinkVertex sinkVertex) {
    	List<String> fieldNames = sinkVertex.getFields().stream().map(SinkVertex.Field::getName).collect(Collectors.toList());
    	List<String> fieldTypes = sinkVertex.getFields().stream().map(SinkVertex.Field::getType).collect(Collectors.toList());
    	List<ModifierType> fieldVisibilitiesList = sinkVertex.getFields().stream().map(SinkVertex.Field::getModifier).collect(Collectors.toList());
    	StringBuilder plantUMLFields = new StringBuilder();
    	for (int i = 0; i < fieldNames.size(); i++) {
			plantUMLFields.append(visibilityToPlantUML(fieldVisibilitiesList.get(i))).append(fieldNames.get(i)).append(": ")
					.append(fieldTypes.get(i)).append("\n");
		}
		return plantUMLFields.toString();
	}
    
    private String convertMethodsToPlantUML(SinkVertex sinkVertex) {
		List<String> methodNames = sinkVertex.getMethods().stream().map(SinkVertex.Method::getName).collect(Collectors.toList());
		List<String> methodReturnTypes = sinkVertex.getMethods().stream().map(SinkVertex.Method::getReturnType).collect(Collectors.toList());
		List<ModifierType> methodVisibilitiesList = sinkVertex.getMethods().stream().map(SinkVertex.Method::getModifierType).collect(Collectors.toList());
		List<String> methodParametersPlantUML = new ArrayList<>();
		StringBuilder plantUMLMethods = new StringBuilder();

		for (SinkVertex.Method method: sinkVertex.getMethods()) {
			methodParametersPlantUML.add(method.getParameters().entrySet()
					.stream()
					.map(parameter -> parameter.getValue() + " " + parameter.getKey())
					.collect(Collectors.joining(", ")));
		}

		for (int i = 0; i < methodNames.size(); i++) {
			plantUMLMethods.append(visibilityToPlantUML(methodVisibilitiesList.get(i))).append(methodNames.get(i))
					.append("(").append(methodParametersPlantUML.get(i)).append("): ").append(methodReturnTypes.get(i)).append("\n");
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
