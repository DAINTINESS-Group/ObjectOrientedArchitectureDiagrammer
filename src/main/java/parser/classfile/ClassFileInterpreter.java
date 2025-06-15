package parser.classfile;

import java.nio.file.Path;
import java.util.Collection;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import parser.Interpreter;

public class ClassFileInterpreter implements Interpreter {
    private final ClassFileParser parser = new ClassFileParser();

    @Override
    public void parseProject(Path sourcePackagePath) {
        parser.parsePackage(sourcePackagePath);
    }

    @Override
    public void convertToGraph(
            Collection<ClassifierVertex> vertices, Collection<PackageVertex> packageVertices) {
        parser.createRelationships(vertices, packageVertices);
    }
}
