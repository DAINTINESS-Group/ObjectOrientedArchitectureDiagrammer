package model.diagram;

import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.tree.Node;
import model.tree.PackageNode;

import java.util.ArrayList;
import java.util.List;

public class ClassDiagram extends Diagram {

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

    public void createCollections() {
        graphNodeCollection = new GraphMLLeafNode();
        graphEdgeCollection = new GraphMLLeafEdge();
    }

}
