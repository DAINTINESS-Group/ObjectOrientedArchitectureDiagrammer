package client;

import controller.DiagramController;
import controller.DiagramControllerFactory;

import java.util.Arrays;
import java.util.List;

public class Client {

	public static void main(String[] args) {

		DiagramControllerFactory diagramControllerFactory = new DiagramControllerFactory();

		DiagramController classDiagramController = diagramControllerFactory.getController("Class");
		classDiagramController.createTree(args[0]);
		classDiagramController.convertTreeToDiagram(Arrays.asList("Command", "EditCommand", "CreateCommand", "CommandFactory"));
		classDiagramController.exportDiagramToGraphML(args[2]);

		DiagramController packageDiagramController = diagramControllerFactory.getController("Package");
		packageDiagramController.createTree(args[0]);
		packageDiagramController.convertTreeToDiagram(List.of(args[1]));
		packageDiagramController.exportDiagramToGraphML(args[2]);
	}

}
