package client;

import controller.ClassDiagramController;
import controller.DiagramController;
import controller.PackageDiagramController;

import java.util.Arrays;
import java.util.List;

public class Client {

	public static void main(String[] args) {

		DiagramController packageDiagramController = new PackageDiagramController();
		packageDiagramController.createTree(args[0]);
		packageDiagramController.convertTreeToDiagram(List.of(args[1]));
		packageDiagramController.exportDiagramToGraphML(args[2]);

		DiagramController classDiagramController = new ClassDiagramController();
		classDiagramController.createTree(args[0]);
		classDiagramController.convertTreeToDiagram(Arrays.asList("Command", "EditCommand", "CreateCommand", "CommandFactory"));
		classDiagramController.exportDiagramToGraphML(args[2]);
	}

}
