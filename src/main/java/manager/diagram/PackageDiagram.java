package manager.diagram;

import model.diagram.graphml.GraphMLPackageEdge;
import model.diagram.graphml.GraphMLPackageNode;
import model.tree.Node;
import model.tree.Relationship;
import model.tree.SourceProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageDiagram extends Diagram {

    public PackageDiagram(SourceProject sourceProject) {
        super(sourceProject);
        graphNodeCollection = new GraphMLPackageNode();
        graphEdgeCollection = new GraphMLPackageEdge();
    }

    public List<Node> getChosenNodes(List<String> chosenPackagesNames) {
        List<Node> chosenPackages = new ArrayList<>();
        for (String chosenPackage: chosenPackagesNames) {
            if (sourceProject.getPackageNodes().get(chosenPackage).isValid()) {
                chosenPackages.add(sourceProject.getPackageNodes().get(chosenPackage));
            }
        }
        return chosenPackages;
    }

    public Map<String, Map<String, String>> convertCollectionsToDiagram() {
        Map<String, Map<String, String>> graph = new HashMap<>();
        for (Node packageNode: graphNodeCollection.getGraphNodes().keySet()) {
            Map<String, String> edgesTemp = new HashMap<>();
            for (Relationship relationship: graphEdgeCollection.getGraphEdges().keySet()){
                if (relationship.getStartingNode().equals(packageNode)) {
                    edgesTemp.put(relationship.getEndingNode().getName(), relationship.getRelationshipType().name());
                }
            }
            graph.put(packageNode.getName(), edgesTemp);
        }
        return graph;
    }
}
