package manager;

import model.diagram.ClassDiagram;
import model.diagram.PackageDiagram;
import parser.Interpreter;

import java.nio.file.Path;

public class SourceProject {

    private final Interpreter interpreter;
    private ClassDiagram classDiagram;
    private PackageDiagram packageDiagram;

    public SourceProject(ClassDiagram classDiagram) {
        interpreter = new Interpreter();
        this.classDiagram = classDiagram;
    }

    public SourceProject(PackageDiagram packageDiagram) {
        interpreter = new Interpreter();
        this.packageDiagram = packageDiagram;
    }


    public void createGraph(Path sourcePackagePath) {
        interpreter.parseProject(sourcePackagePath);
        interpreter.convertTreeToGraph();
    }

    public void setClassDiagramSinkVertices() {
        classDiagram.setSinkVertices(interpreter.getSinkVertices());
    }

    public void setPackageDiagramVertices() {
        packageDiagram.setVertices(interpreter.getVertices());
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

}