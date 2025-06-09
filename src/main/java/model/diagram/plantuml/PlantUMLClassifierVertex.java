package model.diagram.plantuml;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import model.diagram.ClassDiagram;
import model.graph.ClassifierVertex;
import model.graph.ModifierType;

public class PlantUMLClassifierVertex {

    public static StringBuilder convertSinkVertices(ClassDiagram classDiagram) {
        String sinkVertices =
                classDiagram.getDiagram().keySet().stream()
                        .map(
                                it ->
                                        String.format(
                                                "%s %s {\n%s%s}",
                                                it.getVertexType(),
                                                it.getName(),
                                                convertFields(it),
                                                convertMethods(it)))
                        .collect(Collectors.joining("\n\n"));

        return new StringBuilder(sinkVertices);
    }

    private static String convertFields(ClassifierVertex classifierVertex) {
        if (classifierVertex.getFields().isEmpty()) return "";

        return classifierVertex.getFields().stream()
                        .map(
                                it ->
                                        String.format(
                                                "%s%s: %s",
                                                getVisibility(it.modifier()), it.name(), it.type()))
                        .collect(Collectors.joining("\n"))
                + "\n";
    }

    private static String convertMethods(ClassifierVertex classifierVertex) {
        StringBuilder plantUMLMethods = new StringBuilder();

        List<ClassifierVertex.Method> constructors =
                classifierVertex.getMethods().stream()
                        .filter(it -> it.returnType().equals("Constructor"))
                        .sorted(Comparator.comparingInt(it -> it.parameters().size()))
                        .collect(Collectors.toList());

        convertMethod(plantUMLMethods, constructors);

        List<ClassifierVertex.Method> methods =
                classifierVertex.getMethods().stream()
                        .filter(it -> !it.returnType().equals("Constructor"))
                        .collect(Collectors.toList());

        convertMethod(plantUMLMethods, methods);

        return plantUMLMethods.toString();
    }

    private static void convertMethod(
            StringBuilder plantUMLMethods, List<ClassifierVertex.Method> methods) {
        for (ClassifierVertex.Method method : methods) {
            plantUMLMethods
                    .append(getVisibility(method.modifier()))
                    .append(method.name())
                    .append("(")
                    .append(
                            method.parameters().entrySet().stream()
                                    .map(it -> it.getValue() + " " + it.getKey())
                                    .collect(Collectors.joining(", ")))
                    .append("): ")
                    .append(method.returnType())
                    .append("\n");
        }
    }

    private static String getVisibility(ModifierType visibilityEnum) {
        return switch (visibilityEnum) {
            case PRIVATE -> "-";
            case PUBLIC -> "+";
            case PROTECTED -> "#";
            default -> "~";
        };
    }
}
