package manager.diagram;

import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.tree.Node;
import model.tree.PackageNode;
import model.tree.Relationship;
import model.tree.SourceProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassDiagram extends Diagram {

    public ClassDiagram(SourceProject sourceProject) {
        super(sourceProject);
        graphNodeCollection = new GraphMLLeafNode();
        graphEdgeCollection = new GraphMLLeafEdge();
    }

    public List<Node> getChosenNodes(List<String> chosenClassesNames) {
        List<Node> chosenClasses = new ArrayList<>();
        for (String chosenClass: chosenClassesNames) {
            for (PackageNode p: sourceProject.getPackageNodes().values()){
                if (p.getLeafNodes().containsKey(chosenClass)) {
                    chosenClasses.add(p.getLeafNodes().get(chosenClass));
                    break;
                }
            }
        }
        return chosenClasses;
    }

    public Map<String, Map<String, String>> convertCollectionsToDiagram() {
        Map<String, Map<String, String>> graph = new HashMap<>();
        for (Node leafNode: graphNodeCollection.getGraphNodes().keySet()) {
            Map<String, String> edgesTemp = new HashMap<>();
            for (Relationship relationship: graphEdgeCollection.getGraphEdges().keySet()){
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
