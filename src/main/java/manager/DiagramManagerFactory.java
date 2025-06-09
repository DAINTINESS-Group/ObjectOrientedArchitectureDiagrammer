package manager;

public class DiagramManagerFactory {

    public static DiagramManager createDiagramManager(String diagramType) {
        return switch (DiagramType.get(diagramType)) {
            case CLASS -> new ClassDiagramManager();
            case PACKAGE -> new PackageDiagramManager();
        };
    }
}
