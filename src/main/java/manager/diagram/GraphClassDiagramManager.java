package manager.diagram;

import model.diagram.*;
import model.tree.Node;
import model.tree.PackageNode;
import model.tree.Relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphClassDiagramManager extends GraphDiagramManager {

    public GraphClassDiagramManager(Map<String, PackageNode> packageNodes) {
        super(packageNodes);
        graphNode = new GraphMLLeafNode();
        graphEdge = new GraphMLLeafEdge();
    }

    public List<Node> getChosenNodes(List<String> chosenClassesNames) {
        List<Node> chosenClasses = new ArrayList<>();
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

    public Map<String, Map<String, String>> getGraph() {
        Map<String, Map<String, String>> graph = new HashMap<>();
        for (Node leafNode: graphNode.getGraphNodes().keySet()) {
            Map<String, String> edgesTemp = new HashMap<>();
            for (Relationship relationship: graphEdge.getGraphEdges().keySet()){
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
