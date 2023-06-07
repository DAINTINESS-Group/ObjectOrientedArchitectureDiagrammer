package model.diagram.plantuml;

import model.graph.ModifierType;
import model.graph.SinkVertex;

import java.util.Comparator;
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
			plantUMLDeclaration += convertMethodsToPlantUML(node) + "}\n\n";
			plantUMLBuffer.append(plantUMLDeclaration);
		}
		return plantUMLBuffer;
	}
	
    private String convertFieldsToPlantUML(SinkVertex sinkVertex) {
    	StringBuilder plantUMLFields = new StringBuilder();
		for (SinkVertex.Field field: sinkVertex.getFields()) {
			plantUMLFields.append(getVisibility(field.getModifier())).append(field.getName()).append(": ").append(field.getType()).append("\n");
		}
		return plantUMLFields.toString();
	}
    
    private String convertMethodsToPlantUML(SinkVertex sinkVertex) {
		StringBuilder plantUMLMethods = new StringBuilder();
		List<SinkVertex.Method> constructors = sinkVertex.getMethods().stream()
			.filter(method -> method.getReturnType().equals("Constructor"))
			.sorted(Comparator.comparingInt(method -> method.getParameters().size()))
			.collect(Collectors.toList());
		convertMethod(plantUMLMethods, constructors);

		List<SinkVertex.Method> methods = sinkVertex.getMethods().stream()
			.filter(method -> !method.getReturnType().equals("Constructor"))
			.collect(Collectors.toList());
		convertMethod(plantUMLMethods, methods);

		return plantUMLMethods.toString();
    }

	private void convertMethod(StringBuilder plantUMLMethods, List<SinkVertex.Method> constructors) {
		for (SinkVertex.Method method: constructors) {
			plantUMLMethods.append(getVisibility(method.getModifierType())).append(method.getName()).append("(")
				.append(method.getParameters().entrySet()
					.stream()
					.map(parameter -> parameter.getValue() + " " + parameter.getKey())
					.collect(Collectors.joining(", ")))
				.append("): ").append(method.getReturnType()).append("\n");
		}
	}

	private String getVisibility(ModifierType visibilityEnum) {
		return switch (visibilityEnum) {
			case PRIVATE -> "-";
			case PUBLIC -> "+";
			case PROTECTED -> "#";
			default -> "~";
		};
    }

}
