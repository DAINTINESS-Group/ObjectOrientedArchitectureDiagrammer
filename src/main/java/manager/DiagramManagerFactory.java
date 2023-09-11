package manager;

public class DiagramManagerFactory {

    public DiagramManager createDiagramManager(DiagramType diagramType) {
        if (diagramType == DiagramType.CLASS) {
            return new ClassDiagramManager();
        }else if (diagramType == DiagramType.PACKAGE) {
            return new PackageDiagramManager();
        }else {
            throw new RuntimeException();
        }
    }

}
