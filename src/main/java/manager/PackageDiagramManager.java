package manager;

import model.diagram.Diagram;
import model.diagram.PackageDiagram;

public class PackageDiagramManager extends DiagramManager {

    public Diagram getDiagramType() {
        return new PackageDiagram();
    }

}
