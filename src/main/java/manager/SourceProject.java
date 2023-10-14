package manager;

import model.diagram.ClassDiagram;
import model.diagram.PackageDiagram;
import parser.Interpreter;

import java.nio.file.Path;

public class SourceProject {

	private static Interpreter    interpreter;
	private static ClassDiagram   classDiagram;
	private static PackageDiagram packageDiagram;

	public SourceProject(ClassDiagram classDiagram) {
		interpreter 			   = new Interpreter();
		SourceProject.classDiagram = classDiagram;
	}

	public SourceProject(PackageDiagram packageDiagram) {
		interpreter 				 = new Interpreter();
		SourceProject.packageDiagram = packageDiagram;
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