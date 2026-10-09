package gr.uoi.ooad.parser;

import java.nio.file.Path;
import java.util.Collection;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;
import gr.uoi.ooad.parser.ast.ASTParser;
import gr.uoi.ooad.parser.classfile.ClassFileParser;

/**
 * The interpreter is responsible for transforming the given project to a graph. The implementations
 * are based on the type of the input. For source files, see {@link ASTParser} and for class files,
 * see {@link ClassFileParser}
 */
public interface Interpreter {
    void parseProject(Path sourcePackagePath);

    void convertToGraph(
            Collection<ClassifierVertex> vertices, Collection<PackageVertex> packageVertices);
}
