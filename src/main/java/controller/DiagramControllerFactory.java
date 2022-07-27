package controller;

public class DiagramControllerFactory {

    public Controller getController(String type) {

        if (type.equals("Class")) {
            return new ClassDiagramController();
        }else {
            return new PackageDiagramController();
        }
    }
}
