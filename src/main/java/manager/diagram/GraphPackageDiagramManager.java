package manager.diagram;

import model.diagram.*;
import model.tree.Node;
import model.tree.PackageNode;
import model.tree.Relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphPackageDiagramManager extends GraphDiagramManager {

    public GraphPackageDiagramManager(Map<String, PackageNode> packageNodes) {
        super(packageNodes);
        graphNode = new GraphMLPackageNode();
        graphEdge = new GraphMLPackageEdge();
    }

    public List<Node> getChosenNodes(List<String> chosenPackagesNames) {
        List<Node> chosenPackages = new ArrayList<>();
        for (String chosenPackage: chosenPackagesNames) {
            if (packages.get(chosenPackage).isValid()) {
                chosenPackages.add(packages.get(chosenPackage));
            }
        }
        return chosenPackages;
    }

    public Map<String, Map<String, String>> getGraph() {
        Map<String, Map<String, String>> graph = new HashMap<>();
        for (Node packageNode: graphNode.getGraphNodes().keySet()) {
            Map<String, String> edgesTemp = new HashMap<>();
            for (Relationship relationship: graphEdge.getGraphEdges().keySet()){
                if (relationship.getStartingNode().equals(packageNode)) {
                    edgesTemp.put(relationship.getEndingNode().getName(), relationship.getRelationshipType().name());
                }
            }
            graph.put(packageNode.getName(), edgesTemp);
        }
        return graph;
    }

}
