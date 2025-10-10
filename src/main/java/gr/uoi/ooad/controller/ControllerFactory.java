package gr.uoi.ooad.controller;

public class ControllerFactory {

    public static Controller createController(String controllerType, String diagramType) {
        return switch (ControllerType.get(controllerType)) {
            case UML -> new DiagramController(diagramType);
        };
    }
}
