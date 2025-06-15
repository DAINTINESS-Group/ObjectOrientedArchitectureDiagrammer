package model.diagram.plantuml;

import static proguard.classfile.ClassConstants.METHOD_NAME_INIT;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import model.diagram.ClassDiagram;
import model.graph.ClassifierVertex;
import model.graph.ModifierType;

public class PlantUMLClassifierVertex {

    public static StringBuilder convertSinkVertices(ClassDiagram classDiagram) {
        StringBuilder ret = new StringBuilder();
        for (ClassifierVertex it : classDiagram.getDiagram().keySet()) {
            ret.append(it.getVertexType())
                    .append(" ")
                    .append(it.getName())
                    .append(" {")
                    .append(System.lineSeparator())
                    .append(convertFields(it))
                    .append(convertMethods(it))
                    .append("}")
                    .append(System.lineSeparator());
        }

        return new StringBuilder(ret);
    }

    private static String convertFields(ClassifierVertex classifierVertex) {
        StringBuilder ret = new StringBuilder();
        for (ClassifierVertex.Field it : classifierVertex.getFields()) {
            ret.append(getVisibility(it.modifier()))
                    .append(it.name())
                    .append(": ")
                    .append(it.type())
                    .append(System.lineSeparator());
        }
        return ret.toString();
    }

    private static String convertMethods(ClassifierVertex classifierVertex) {
        StringBuilder plantUMLMethods = new StringBuilder();

        List<ClassifierVertex.Method> constructors =
                classifierVertex.getMethods().stream()
                        .filter(
                                it ->
                                        it.returnType().equals("Constructor")
                                                || it.name().equals(METHOD_NAME_INIT))
                        .sorted(Comparator.comparingInt(it -> it.parameters().size()))
                        .collect(Collectors.toList());

        convertMethod(plantUMLMethods, constructors);

        List<ClassifierVertex.Method> methods =
                classifierVertex.getMethods().stream()
                        .filter(
                                it ->
                                        !it.returnType().equals("Constructor")
                                                && !it.name().equals(METHOD_NAME_INIT))
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
                    .append(String.join(", ", method.parameters()))
                    .append("): ")
                    .append(method.returnType())
                    .append(System.lineSeparator());
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
