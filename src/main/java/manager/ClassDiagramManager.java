package manager;

import model.diagram.ClassDiagram;
import model.diagram.Diagram;

public class ClassDiagramManager extends DiagramManager {

    public Diagram getDiagram() {
        return new ClassDiagram();
    }

}
