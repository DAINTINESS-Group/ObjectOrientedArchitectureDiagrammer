package manager;

import model.diagram.ClassDiagram;
import model.diagram.PackageDiagram;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import parser.Interpreter;

import java.nio.file.Path;
import java.util.Map;

public class SourceProject
{

    private final Interpreter interpreter;


    public SourceProject()
    {
        interpreter = new Interpreter();
    }


    public Map<Path, ClassifierVertex> createClassGraph(Path         sourcePackagePath,
                                                        ClassDiagram classDiagram)
    {
        interpreter.parseProject(sourcePackagePath);
        interpreter.convertTreeToGraph();
        Map<Path, ClassifierVertex> sinkVertices = interpreter.getSinkVertices();
        classDiagram.setSinkVertices(sinkVertices);
        return sinkVertices;
    }


    public Map<Path, PackageVertex> createPackageGraph(Path           sourcePackagePath,
                                                       PackageDiagram packageDiagram)
    {
        interpreter.parseProject(sourcePackagePath);
        interpreter.convertTreeToGraph();
        Map<Path, PackageVertex> vertices = interpreter.getVertices();
        packageDiagram.setVertices(vertices);
        return vertices;
    }

}