package gr.uoi.ooad.parser.classfile;

import java.nio.file.Path;
import java.util.Collection;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;
import gr.uoi.ooad.parser.Interpreter;

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
