package client;

import controller.Controller;
import controller.DiagramController;

import java.util.Arrays;
import java.util.List;

public class Client {

	public static void main(String[] args) {
		DiagramController diagramController = new Controller();
		diagramController.createTree(args[0]);
		diagramController.convertTreeToPackageDiagram(List.of(args[1]));
		// diagramController.convertTreeToClassDiagram(Arrays.asList("Command", "EditCommand", "CreateCommand", "CommandFactory"));
		diagramController.exportDiagramToGraphML(args[2]);
	}

}
