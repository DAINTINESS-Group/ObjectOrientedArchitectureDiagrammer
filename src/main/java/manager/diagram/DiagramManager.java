package manager.diagram;

import model.PackageNode;

import java.util.*;
import java.util.List;

public abstract class DiagramManager implements GraphMLDiagramManager {

    protected final Map<String, PackageNode> packages;
    protected Map<Integer, List<Double>> nodesGeometry;

    public DiagramManager (Map<String, PackageNode> packageNodes) {
        this.packages = packageNodes;
    }

    public abstract void createDiagram(List<String> chosenFilesNames);

    public abstract void arrangeDiagram();

    public abstract void exportDiagramToGraphML(String graphMLSavePath);
}