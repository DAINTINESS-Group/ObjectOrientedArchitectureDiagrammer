package model.diagram.plantuml;

import model.diagram.ClassDiagram;
import model.graph.ModifierType;
import model.graph.SinkVertex;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlantUMLSinkVertex {


	private final ClassDiagram classDiagram;

	public PlantUMLSinkVertex(ClassDiagram diagram) {
		classDiagram = diagram;
	}

	public StringBuilder convertSinkVertex() {
		return new StringBuilder(
			classDiagram.getDiagram().keySet().stream()
				.map(sinkVertex ->
					sinkVertex.getVertexType().toString().toLowerCase() + " " + sinkVertex.getName() + " {\n" +
					convertFields(sinkVertex) + convertMethods(sinkVertex) + "}")
				.collect(Collectors.joining("\n\n"))
		);
	}
	
    private String convertFields(SinkVertex sinkVertex) {
		if (sinkVertex.getFields().size() == 0) {
			return "";
		}
		return sinkVertex.getFields().stream()
			.map(field -> getVisibility(field.getModifier()) + field.getName() + ": " + field.getType())
			.collect(Collectors.joining("\n")) + "\n";
	}
    
    private String convertMethods(SinkVertex sinkVertex) {
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

	private void convertMethod(StringBuilder plantUMLMethods, List<SinkVertex.Method> methods) {
		for (SinkVertex.Method method: methods) {
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
