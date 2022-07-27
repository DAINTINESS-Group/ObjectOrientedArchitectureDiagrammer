package manager.diagram;

import model.diagram.*;
import model.tree.Node;
import model.tree.PackageNode;

import java.io.File;
import java.util.*;
import java.util.List;

public abstract class GraphDiagramManager implements DiagramManager {

    protected final Map<String, PackageNode> packages;
    protected Map<Integer, List<Double>> nodesGeometry;
    protected GraphNode graphNode;
    protected GraphEdge graphEdge;

    public GraphDiagramManager(Map<String, PackageNode> packageNodes) {
        this.packages = packageNodes;
    }

    public GraphEdgePair<GraphNode, GraphEdge> createDiagram(List<String> chosenFilesNames){
        graphNode.populateGraphNodes(getChosenNodes(chosenFilesNames));
        graphEdge.setGraphNodes(graphNode.getGraphNodes());
        graphEdge.populateGraphEdges(getChosenNodes(chosenFilesNames));
        return new GraphEdgePair<>(graphNode, graphEdge);
    }

    public Map<Integer, List<Double>> arrangeDiagram(){
        DiagramArrangement diagramArrangement = new DiagramArrangement();
        diagramArrangement.arrangeDiagram(graphNode.getGraphNodes(), graphEdge.getGraphEdges());
        nodesGeometry = diagramArrangement.getNodesGeometry();
        return nodesGeometry;
    }

    public File exportDiagramToGraphML(String graphMLSavePath){
        graphNode.convertNodesToGraphML(nodesGeometry);
        graphEdge.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphNode.getGraphMLBuffer(), graphEdge.getGraphMLBuffer());
        return graphMLExporter.getExportedGraphMLFile();
    }

    public abstract Map<String, Map<String, String>> getGraph();

    public abstract List<Node> getChosenNodes(List<String> chosenFileNames);
}