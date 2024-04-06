package parser;

import java.nio.file.Path;
import parser.ast.ASTParser;
import parser.classfile.ClassFileParser;

/**
 * The interpreter is responsible for transforming the given project to a graph. The implementations
 * are based on the type of the input. For source files, see {@link ASTParser} and for class files,
 * see {@link ClassFileParser}
 */
public interface Interpreter {
    void parseProject(Path sourcePackagePath);

    void convertToGraph();
}
