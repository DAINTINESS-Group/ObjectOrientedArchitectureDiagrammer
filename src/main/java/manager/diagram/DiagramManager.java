package manager.diagram;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;
import model.diagram.GraphLayoutPair;
import model.tree.PackageNode;

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

    public abstract Map<String, Map<String, String>> getGraph();

}