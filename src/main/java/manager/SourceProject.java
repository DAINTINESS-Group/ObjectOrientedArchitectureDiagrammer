package manager;

import model.diagram.ClassDiagram;
import model.diagram.PackageDiagram;
import parser.Interpreter;

import java.nio.file.Path;

public class SourceProject {

	private final Interpreter interpreter = new Interpreter();

	public void createClassGraph(Path sourcePackagePath, ClassDiagram classDiagram) {
		interpreter.parseProject(sourcePackagePath);
		interpreter.convertTreeToGraph();
		classDiagram.setSinkVertices(interpreter.getSinkVertices());
	}

	public void createPackageGraph(Path sourcePackagePath, PackageDiagram packageDiagram) {
		interpreter.parseProject(sourcePackagePath);
		interpreter.convertTreeToGraph();
		packageDiagram.setVertices(interpreter.getVertices());
	}

	public Interpreter getInterpreter() {
		return interpreter;
	}

}