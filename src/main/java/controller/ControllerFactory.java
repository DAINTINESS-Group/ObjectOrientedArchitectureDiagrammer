package controller;

public class ControllerFactory {

	public static Controller createController(String controllerType, String diagramType) {
		if (ControllerType.get(controllerType) == ControllerType.UML) {
			return new DiagramController(diagramType);
		}else {
			throw new RuntimeException();
		}
	}
}
