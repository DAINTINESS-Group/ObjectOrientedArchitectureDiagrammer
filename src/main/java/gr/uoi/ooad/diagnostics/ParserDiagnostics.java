package gr.uoi.ooad.diagnostics;

import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.parser.ast.ASTInterpreter;
import java.nio.file.Files;
import java.nio.file.Path;
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


        printArcs(sinkVertices);
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

    private static void printArcs(List<ClassifierVertex> sinkVertices){
        for (ClassifierVertex sinkVertex: sinkVertices) {
            for (Arc<ClassifierVertex> arc : sinkVertex.getArcs()) {
                System.out.println(
                        arc.sourceVertex().getName()
                                + " - "
                                + arc.arcType()
                                + " -> "
                                + arc.targetVertex().getName());

            }
        }
    }

}
