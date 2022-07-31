package manager.diagram;

import model.diagram.ClassDiagram;
import model.diagram.Diagram;

public class ClassDiagramManager extends DiagramManager {

    public Diagram getDiagramType() {
        return new ClassDiagram();
    }

}
