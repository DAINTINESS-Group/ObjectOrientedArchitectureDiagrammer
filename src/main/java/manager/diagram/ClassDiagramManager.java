package manager.diagram;

import model.LeafNode;
import model.PackageNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassDiagramManager extends DiagramManager{

    private final GraphMLNode<LeafNode> graphMLNode;
    private final GraphMLLeafEdge graphMLEdge;

    public ClassDiagramManager(Map<String, PackageNode> packageNodes) {
        super(packageNodes);
        graphMLNode = new GraphMLLeafNode<>();
        graphMLEdge = new GraphMLLeafEdge();
    }

    public void createDiagram(List<String> chosenClassesNames) {
        graphMLNode.populateGraphMLNodes(getChosenClasses(chosenClassesNames));
        graphMLEdge.setGraphMLNodes(graphMLNode.getGraphMLNodes());
        graphMLEdge.populateGraphMLEdges(getChosenClasses(chosenClassesNames));
    }

    private List<LeafNode> getChosenClasses(List<String> chosenClassesNames) {
        List<LeafNode> chosenClasses = new ArrayList<>();
        for (String chosenClass: chosenClassesNames) {
            for (PackageNode p: packages.values()){
                if (p.getLeafNodes().containsKey(chosenClass)) {
                    chosenClasses.add(p.getLeafNodes().get(chosenClass));
                    break;
                }
            }
        }
        return chosenClasses;
    }

    public void arrangeDiagram() {
        DiagramArrangement<LeafNode> diagramArrangement = new ClassDiagramArrangement<>();
        diagramArrangement.arrangeDiagram(graphMLNode.getGraphMLNodes(), graphMLEdge.getGraphMLEdges());
        nodesGeometry = diagramArrangement.getNodesGeometry();
    }

    public void exportDiagramToGraphML(String graphMLSavePath) {
        graphMLNode.convertNodesToGraphML(nodesGeometry);
        graphMLEdge.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphMLNode.getGraphMLBuffer(), graphMLEdge.getGraphMLBuffer());
    }
}
