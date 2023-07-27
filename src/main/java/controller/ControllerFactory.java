package controller;

public class ControllerFactory {
	
	public Controller createController(String controllerType, String diagramType) {
        if (ControllerType.valueOf(controllerType.toUpperCase()).equals(ControllerType.UML_DIAGRAM)) {
            return new DiagramController(diagramType);
        }else {
            throw new RuntimeException();
        }
    }
}
