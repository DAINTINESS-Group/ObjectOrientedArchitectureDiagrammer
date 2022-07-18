package client;

import controller.DiagramController;
import controller.DiagramControllerFactory;

import java.util.Arrays;

public class Client {

	public static void main(String[] args) {

		DiagramControllerFactory diagramControllerFactory = new DiagramControllerFactory();

		DiagramController classDiagramController = diagramControllerFactory.getController("Class");
		classDiagramController.createTree(args[0]);
		classDiagramController.convertTreeToDiagram(Arrays.asList("Command", "EditCommand", "CreateCommand", "CommandFactory", "LatexEditorController", "CommandFactory"));
		classDiagramController.arrangeDiagram();
		classDiagramController.exportDiagramToGraphML(args[2]);

		DiagramController packageDiagramController = diagramControllerFactory.getController("Package");
		packageDiagramController.createTree(args[0]);
		packageDiagramController.convertTreeToDiagram(Arrays.asList("model", "controller"));
		packageDiagramController.arrangeDiagram();
		packageDiagramController.exportDiagramToGraphML(args[2]);

	}

}
