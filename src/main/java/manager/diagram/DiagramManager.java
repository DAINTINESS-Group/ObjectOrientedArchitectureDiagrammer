package manager.diagram;

import model.PackageNode;

import java.util.*;
import java.util.List;

public abstract class DiagramManager implements GraphMLDiagramManager {

    protected final GraphMLNode graphMLNode;
    protected final GraphMLEdge graphMLEdge;
    protected final GraphMLPackageNode graphMLPackageNode;
    protected final GraphMLPackageEdge graphMLPackageEdge;

    protected final Map<String, PackageNode> packages;
    protected Map<Integer, List<Double>> nodesGeometry;

    public DiagramManager (Map<String, PackageNode> packageNodes) {
        graphMLNode = new GraphMLNode();
        graphMLEdge = new GraphMLEdge();
        graphMLPackageNode = new GraphMLPackageNode();
        graphMLPackageEdge = new GraphMLPackageEdge();
        this.packages = packageNodes;
    }

    public abstract void createDiagram(List<String> chosenFilesNames);

    public abstract void arrangeDiagram();

    public abstract void exportDiagramToGraphML(String graphMLSavePath);
}