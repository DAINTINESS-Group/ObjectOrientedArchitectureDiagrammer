package controller;

public class ControllerFactory {

	public static Controller createController(String controllerType, String diagramType) {
		ControllerType controllerTypeEnum = ControllerType.get(controllerType);
		if (controllerTypeEnum == ControllerType.UML) {
			return new DiagramController(diagramType);
		}else {
			throw new RuntimeException();
		}
	}
}
