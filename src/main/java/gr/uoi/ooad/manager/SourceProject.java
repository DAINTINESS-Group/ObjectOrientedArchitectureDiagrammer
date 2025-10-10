package gr.uoi.ooad.manager;

import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;
import gr.uoi.ooad.parser.Interpreter;
import java.nio.file.Path;
import java.util.Map;

public class SourceProject {

    private final Interpreter interpreter;

    public SourceProject() {
        interpreter = new Interpreter();
    }

    public Map<Path, ClassifierVertex> createClassGraph(
            Path sourcePackagePath, ClassDiagram classDiagram) {
        interpreter.parseProject(sourcePackagePath);
        interpreter.convertTreeToGraph();
        Map<Path, ClassifierVertex> sinkVertices = interpreter.getSinkVertices();
        classDiagram.setSinkVertices(sinkVertices);
        return sinkVertices;
    }

    public Map<Path, PackageVertex> createPackageGraph(
            Path sourcePackagePath, PackageDiagram packageDiagram) {
        interpreter.parseProject(sourcePackagePath);
        interpreter.convertTreeToGraph();
        Map<Path, PackageVertex> vertices = interpreter.getVertices();
        packageDiagram.setVertices(vertices);
        return vertices;
    }
}
