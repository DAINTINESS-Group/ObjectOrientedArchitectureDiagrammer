package manager.diagram;

import model.tree.SourceProject;

public class GraphPackageDiagramManager extends GraphDiagramManager {

    public GraphPackageDiagramManager(SourceProject sourceProject) {
        super(sourceProject);
    }

    public Diagram specifyDiagramType() {
        return new PackageDiagram(sourceProject);
    }

}
