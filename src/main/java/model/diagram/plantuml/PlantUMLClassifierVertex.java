package model.diagram.plantuml;

import model.diagram.ClassDiagram;
import model.graph.ClassifierVertex;
import model.graph.ModifierType;

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
				this.classDiagram.getDiagram().keySet()
						.stream()
						.map(sinkVertex ->
							sinkVertex.getVertexType().toString().toLowerCase() + " " + sinkVertex.getName() + " {\n" +
							convertFields(sinkVertex) + convertMethods(sinkVertex) + "}")
						.collect(Collectors.joining("\n\n"))
		);
	}

	private String convertFields(ClassifierVertex classifierVertex) {
		if (classifierVertex.getFields().isEmpty()) {
			return "";
		}
		return classifierVertex.getFields()
			   .stream()
			   .map(field ->
					getVisibility(field.modifier()) + field.name() + ": " + field.type())
			   .collect(Collectors.joining("\n")) + "\n";
	}

	private String convertMethods(ClassifierVertex classifierVertex) {
		StringBuilder plantUMLMethods = new StringBuilder();
		List<ClassifierVertex.Method> constructors = classifierVertex.getMethods()
			.stream()
			.filter(method -> method.returnType().equals("Constructor"))
			.sorted(Comparator.comparingInt(method -> method.parameters().size()))
			.collect(Collectors.toList());
		convertMethod(plantUMLMethods, constructors);

		List<ClassifierVertex.Method> methods = classifierVertex.getMethods()
			.stream()
			.filter(method -> !method.returnType().equals("Constructor"))
			.collect(Collectors.toList());
		convertMethod(plantUMLMethods, methods);

		return plantUMLMethods.toString();
	}

	private void convertMethod(StringBuilder plantUMLMethods, List<ClassifierVertex.Method> methods) {
		for (ClassifierVertex.Method method: methods) {
			plantUMLMethods.append(getVisibility(method.modifier())).append(method.name()).append("(")
			.append(method.parameters().entrySet()
				.stream()
				.map(parameter -> parameter.getValue() + " " + parameter.getKey())
				.collect(Collectors.joining(", ")))
			.append("): ").append(method.returnType()).append("\n");
		}
	}

	private String getVisibility(ModifierType visibilityEnum) {
		return switch (visibilityEnum) {
			case PRIVATE   -> "-";
			case PUBLIC    -> "+";
			case PROTECTED -> "#";
			default 	   -> "~";
		};
	}

}
