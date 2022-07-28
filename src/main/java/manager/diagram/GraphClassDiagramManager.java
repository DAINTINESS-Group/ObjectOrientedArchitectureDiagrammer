package manager.diagram;

import model.tree.SourceProject;

public class GraphClassDiagramManager extends GraphDiagramManager {

    public GraphClassDiagramManager(SourceProject sourceProject) {
        super(sourceProject);
    }

    public Diagram specifyDiagramType() {
        return new ClassDiagram(sourceProject);
    }

}
