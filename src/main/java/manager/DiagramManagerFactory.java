package manager;

public class DiagramManagerFactory {

	public static DiagramManager createDiagramManager(String diagramType) {
		DiagramType diagramEnumType = DiagramType.get(diagramType);
		if (diagramEnumType == DiagramType.CLASS) {
			return new ClassDiagramManager();
		}else if (diagramEnumType == DiagramType.PACKAGE) {
			return new PackageDiagramManager();
		}else {
			throw new RuntimeException();
		}
	}

}
