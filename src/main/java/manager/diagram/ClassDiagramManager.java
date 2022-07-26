package manager.diagram;

import model.diagram.*;
import model.tree.LeafNode;
import model.tree.PackageNode;
import model.tree.Relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDiagramManager extends DiagramManager{

    private final GraphMLNode<LeafNode> graphMLLeafNode;
    private final GraphMLLeafEdge graphMLEdge;

    public ClassDiagramManager(Map<String, PackageNode> packageNodes) {
        super(packageNodes);
        graphMLLeafNode = new GraphMLLeafNode<>();
        graphMLEdge = new GraphMLLeafEdge();
    }

    public void createDiagram(List<String> chosenClassesNames) {
        graphMLLeafNode.populateGraphMLNodes(getChosenClasses(chosenClassesNames));
        graphMLEdge.setGraphMLNodes(graphMLLeafNode.getGraphMLNodes());
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
        diagramArrangement.arrangeDiagram(graphMLLeafNode.getGraphMLNodes(), graphMLEdge.getGraphMLEdges());
        nodesGeometry = diagramArrangement.getNodesGeometry();
    }

    public void exportDiagramToGraphML(String graphMLSavePath) {
        graphMLLeafNode.convertNodesToGraphML(nodesGeometry);
        graphMLEdge.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphMLLeafNode.getGraphMLBuffer(), graphMLEdge.getGraphMLBuffer());
    }

    public Map<String, Map<String, String>> getGraph() {
        Map<String, Map<String, String>> graph = new HashMap<>();
        for (LeafNode leafNode: graphMLLeafNode.getGraphMLNodes().keySet()) {
            Map<String, String> edgesTemp = new HashMap<>();
            for (Relationship<LeafNode> relationship: graphMLEdge.getGraphMLEdges().keySet()){
                if (relationship.getStartingNode().equals(leafNode)) {
                    edgesTemp.put(relationship.getEndingNode().getName() + "_" + relationship.getEndingNode().getType().name(),
                            relationship.getRelationshipType().name());
                }
            }
            graph.put(leafNode.getName() + "_" + leafNode.getType().name(), edgesTemp);
        }
        return graph;
    }
}
