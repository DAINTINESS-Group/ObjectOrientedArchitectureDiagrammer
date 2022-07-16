package manager.diagram;

import model.PackageNode;

import java.util.*;
import java.util.List;

public abstract class DiagramManager implements GraphMLDiagramManager {

    protected final GraphMLNode graphMLNode;
    protected final GraphMLEdge graphMLEdge;
    protected final Map<String, PackageNode> packages;
    private Map<Integer, List<Double>> nodesGeometry;

    public DiagramManager (Map<String, PackageNode> packageNodes) {
        graphMLNode = new GraphMLNode();
        graphMLEdge = new GraphMLEdge();
        this.packages = packageNodes;
    }

    public void createDiagram(List<String> chosenFilesNames) {
        parseChosenFiles(chosenFilesNames);
        DiagramArrangement diagramArrangement = new DiagramArrangement();
        diagramArrangement.arrangeDiagram(graphMLNode.getGraphMLNodes(), graphMLEdge.getGraphMLEdges());
        nodesGeometry = diagramArrangement.getNodesGeometry();
    }

    public void exportDiagramToGraphML(String graphMLSavePath) {
        graphMLNode.convertNodesToGraphML(nodesGeometry);
        graphMLEdge.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphMLNode.getGraphMLBuffer(), graphMLEdge.getGraphMLBuffer());
    }

    public abstract void parseChosenFiles(List<String> chosenClassesNames);

}