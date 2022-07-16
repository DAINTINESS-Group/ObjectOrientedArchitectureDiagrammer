package controller;

public class DiagramControllerFactory {

    public DiagramController getController(String type) {

        if (type.equals("Class")) {
            return new ClassDiagramController();
        }else {
            return new PackageDiagramController();
        }
    }
}
