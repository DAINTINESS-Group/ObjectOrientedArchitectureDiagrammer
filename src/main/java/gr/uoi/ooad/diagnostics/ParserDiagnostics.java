package gr.uoi.ooad.diagnostics;

import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.parser.ast.ASTInterpreter;
import gr.uoi.ooad.parser.ast.tree.LeafNode;
import gr.uoi.ooad.parser.ast.tree.PackageNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Prints a diagnostic report of the class graph that the AST parser produces for a Java project.
 *
 * <p>It runs the whole pipeline through {@link ASTInterpreter} (parsing, relationship
 * identification, graph conversion) and then prints the classes that reached the graph, every arc
 * between them, and the number of arcs per {@link ArcType} , including the types with zero arcs.
 *
 * <p>Usage: run it as a Java application with the project's source folder as the only argument,
 * e.g. {@code src/test/resources/ParserTesting}.
 */
public class ParserDiagnostics {

    private static final Logger logger = LogManager.getLogger(ParserDiagnostics.class);
    /**
     * Runs the diagnostics on the project given as the first argument.
     *
     * @param args {@code args[0]} is the path of the project's source folder, absolute or relative
     *     to the working directory.
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please give me the project path as an argument.");
            return;
        }

        Path projectPath = Path.of(args[0]);

        if (!Files.exists(projectPath)) {
            System.out.println("Project path does not exist. " + projectPath.toAbsolutePath());
            return;
        }

        System.out.println("Project path: " + projectPath.toAbsolutePath());

        ASTInterpreter interpreter = new ASTInterpreter();
        interpreter.parseProject(projectPath);
        interpreter.convertToGraph(Collections.emptyList(), Collections.emptyList());

        List<ClassifierVertex> sinkVertices = interpreter.getSinkVertices();

        System.out.println("Classes in graph: " + sinkVertices.size());

        for (ClassifierVertex sinkVertex : sinkVertices) {
            System.out.println("- " + sinkVertex.getName());
        }

        logLines(allLines(interpreter));

        Map<ArcType, Integer> arcCounts = countArcs(sinkVertices);


        for (ArcType type : ArcType.values()) {
            int count = arcCounts.getOrDefault(type, 0);
            System.out.printf("%-15s %d%n", type, count);
        }
    }

    private static Map<ArcType, Integer> countArcs(List<ClassifierVertex> sinkVertices){
        Map<ArcType, Integer> arcCounts = new EnumMap<>(ArcType.class);

        for (ClassifierVertex sinkVertex : sinkVertices) {
            for (Arc<ClassifierVertex> arc : sinkVertex.getArcs()) {
                arcCounts.merge(arc.arcType(), 1, Integer::sum);
            }
        }
        return arcCounts;
    }

    static List<String> foundLines(List<ClassifierVertex> sinkVertices) {
        List<String> lines = new ArrayList<>();
        for (ClassifierVertex sinkVertex : sinkVertices) {
            for (Arc<ClassifierVertex> arc : sinkVertex.getArcs()) {
                lines.add(
                        "FOUND "
                                + arc.arcType()
                                + " "
                                + arc.sourceVertex().getName()
                                + " -> "
                                + arc.targetVertex().getName());
            }
        }
        return lines;
    }

    static List<String> unresolvedLines(ASTInterpreter interpreter) {
        List<String> foundLines = foundLines(interpreter.getSinkVertices());
        List<String> classNames = new ArrayList<>();
        for (ClassifierVertex sinkVertex : interpreter.getSinkVertices()) {
            classNames.add(sinkVertex.getName());
        }
        List<String> lines = new ArrayList<>();

        for (PackageNode packageNode : interpreter.getPackageNodes().values()) {
            for (LeafNode leafNode : packageNode.getLeafNodes().values()) {
                for (Parent parent : parentsOf(leafNode)) {
                    String target = targetName(leafNode.nodeName(), parent.name(), classNames);
                    String relation = parent.type() + " " + leafNode.nodeName() + " -> " + target;
                    if (!foundLines.contains("FOUND " + relation)) {
                        if (classNames.contains(target)) {
                            lines.add("MISSED " + relation);
                        } else {
                            lines.add("EXTERNAL " + relation);
                        }
                    }
                }
            }
        }
        return lines;
    }

    private static String targetName(String source, String parent, List<String> classNames) {
        String innerName = source.split("::")[0] + "::" + parent;
        if (classNames.contains(innerName)) {
            return innerName;
        }
        return parent;
    }

    private static List<Parent> parentsOf(LeafNode leafNode) {
        List<Parent> parents = new ArrayList<>();
        if (!leafNode.baseClass().isEmpty()) {
            parents.add(new Parent(ArcType.EXTENSION, leafNode.baseClass()));
        }

        for (String implementedInterface : leafNode.implementedInterfaces()) {
            parents.add(new Parent(ArcType.IMPLEMENTATION, implementedInterface));
        }
        return parents;
    }

    public static void logDiagram(ClassDiagram classDiagram) {
        List<String> classNames = new ArrayList<>();
        for (ClassifierVertex vertex : classDiagram.getGraphNodes().keySet()) {
            classNames.add(vertex.getName());
        }
        Collections.sort(classNames);

        logger.info("DIAGRAM " + String.join(", ", classNames));
        logLines(chosenLines(classDiagram.getDiagnosticLines(), classNames));
    }

    private static void logLines(List<String> lines) {
        for (String line : lines) {
            if (line.startsWith("MISSED")) {
                logger.warn(line);
            } else {
                logger.info(line);
            }
        }
    }

    public static List<String> allLines(ASTInterpreter interpreter) {
        List<String> lines = foundLines(interpreter.getSinkVertices());
        lines.addAll(unresolvedLines(interpreter));
        return lines;
    }

    static List<String> chosenLines(List<String> lines, List<String> classNames) {
        List<String> chosenLines = new ArrayList<>();
        for (String line : lines) {
            String[] words = line.split(" ");
            String source = words[2];
            String target = words[4];
            if (line.startsWith("FOUND")) {
                if (classNames.contains(source) && classNames.contains(target)) {
                    chosenLines.add(line);
                }
            } else if (classNames.contains(source)) {
                chosenLines.add(line);
            }
        }
        return chosenLines;
    }

    private record Parent(ArcType type, String name) {}
}
