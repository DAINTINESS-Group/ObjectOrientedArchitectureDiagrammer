package manager;

public class DiagramManagerFactory {

    public DiagramManager createDiagramManager(String diagramType) {
        if (DiagramType.valueOf(diagramType.toUpperCase()).equals(DiagramType.CLASS)) {
            return new ClassDiagramManager();
        }else if (DiagramType.valueOf(diagramType.toUpperCase()).equals(DiagramType.PACKAGE)) {
            return new PackageDiagramManager();
        }else {
            throw new RuntimeException();
        }
    }

}
