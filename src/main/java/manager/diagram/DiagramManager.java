package manager.diagram;

import model.diagram.DiagramArrangement;
import model.diagram.GraphEdge;
import model.diagram.GraphMLExporter;
import model.diagram.GraphNode;
import model.tree.Node;
import model.tree.PackageNode;

import java.util.*;
import java.util.List;

public abstract class DiagramManager implements GraphMLDiagramManager {

    protected final Map<String, PackageNode> packages;
    protected Map<Integer, List<Double>> nodesGeometry;
    protected GraphNode graphNode;
    protected GraphEdge graphEdge;

    public DiagramManager (Map<String, PackageNode> packageNodes) {
        this.packages = packageNodes;
    }

    public void createDiagram(List<String> chosenFilesNames){
        graphNode.populateGraphMLNodes(getChosenNodes(chosenFilesNames));
        graphEdge.setGraphNodes(graphNode.getGraphNodes());
        graphEdge.populateGraphMLEdges(getChosenNodes(chosenFilesNames));
    }

    public void arrangeDiagram(){
        DiagramArrangement diagramArrangement = new DiagramArrangement();
        diagramArrangement.arrangeDiagram(graphNode.getGraphNodes(), graphEdge.getGraphEdges());
        nodesGeometry = diagramArrangement.getNodesGeometry();
    }

    public void exportDiagramToGraphML(String graphMLSavePath){
        graphNode.convertNodesToGraphML(nodesGeometry);
        graphEdge.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphNode.getGraphMLBuffer(), graphEdge.getGraphMLBuffer());
    }

    public abstract Map<String, Map<String, String>> getGraph();

    public abstract List<Node> getChosenNodes(List<String> chosenFileNames);
}