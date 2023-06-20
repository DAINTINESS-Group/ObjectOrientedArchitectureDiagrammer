package model.diagram.plantuml;

import model.diagram.ClassDiagram;
import model.graph.ModifierType;
import model.graph.ClassifierVertex;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlantUMLClassifierVertex {


	private final ClassDiagram classDiagram;

	public PlantUMLClassifierVertex(ClassDiagram diagram) {
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
	
    private String convertFields(ClassifierVertex classifierVertex) {
		if (classifierVertex.getFields().size() == 0) {
			return "";
		}
		return classifierVertex.getFields().stream()
			.map(field -> getVisibility(field.getModifier()) + field.getName() + ": " + field.getType())
			.collect(Collectors.joining("\n")) + "\n";
	}
    
    private String convertMethods(ClassifierVertex classifierVertex) {
		StringBuilder plantUMLMethods = new StringBuilder();
		List<ClassifierVertex.Method> constructors = classifierVertex.getMethods().stream()
			.filter(method -> method.getReturnType().equals("Constructor"))
			.sorted(Comparator.comparingInt(method -> method.getParameters().size()))
			.collect(Collectors.toList());
		convertMethod(plantUMLMethods, constructors);

		List<ClassifierVertex.Method> methods = classifierVertex.getMethods().stream()
			.filter(method -> !method.getReturnType().equals("Constructor"))
			.collect(Collectors.toList());
		convertMethod(plantUMLMethods, methods);

		return plantUMLMethods.toString();
    }

	private void convertMethod(StringBuilder plantUMLMethods, List<ClassifierVertex.Method> methods) {
		for (ClassifierVertex.Method method: methods) {
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
