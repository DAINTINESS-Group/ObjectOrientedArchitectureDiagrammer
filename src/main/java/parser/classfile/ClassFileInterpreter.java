package parser.classfile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import parser.Interpreter;

public class ClassFileInterpreter implements Interpreter {
    private final ClassFileParser parser = new ClassFileParser();
    private final List<PackageVertex> packageNodeVertexMap = new ArrayList<>();
    private final List<ClassifierVertex> leafNodeSinkVertexMap = new ArrayList<>();

    @Override
    public void parseProject(Path sourcePackagePath) {
        parser.parsePackage(sourcePackagePath);
    }

    @Override
    public void convertToGraph() {
        parser.createRelationships(leafNodeSinkVertexMap, packageNodeVertexMap);
    }

    public List<ClassifierVertex> getSinkVertices() {
        return leafNodeSinkVertexMap;
    }

    public List<PackageVertex> getVertices() {
        return packageNodeVertexMap;
    }
}
