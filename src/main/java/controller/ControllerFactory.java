package controller;

public class ControllerFactory {
	
	public Controller createController(ControllerType controllerType, String diagramType) {
		if (controllerType == ControllerType.UML_DIAGRAM) {
			return new DiagramController(diagramType);
		}else {
			throw new RuntimeException();
		}
	}
}
