package model.diagram;

import model.diagram.graphml.GraphMLPackageEdge;
import model.diagram.graphml.GraphMLPackageNode;
import model.tree.Node;

import java.util.ArrayList;
import java.util.List;

public class PackageDiagram extends Diagram {

    public PackageDiagram() {
        super();
        graphNodeCollection = new GraphMLPackageNode();
        graphEdgeCollection = new GraphMLPackageEdge();
    }

    public List<Node> getChosenNodes(List<String> chosenPackagesNames) {
        List<Node> chosenPackages = new ArrayList<>();
        for (String chosenPackage: chosenPackagesNames) {
            if (isPackageValid(chosenPackage)) {
                if (doesPackageHaveParentNode(chosenPackage)) {
                    chosenPackages.add(sourceProject.getPackageNodes().get(getPackageName(chosenPackage)));
                }else {
                    chosenPackages.add(sourceProject.getPackageNodes().get(chosenPackage));
                }
            }
        }
        return chosenPackages;
    }

    private boolean isPackageValid(String chosenPackage) {
        if (doesPackageHaveParentNode(chosenPackage)) {
            return sourceProject.getPackageNodes().get(getPackageName(chosenPackage)).isValid();
        }else {
            return sourceProject.getPackageNodes().get(chosenPackage).isValid();
        }
    }

    private boolean doesPackageHaveParentNode(String chosenPackage) {
        return chosenPackage.contains(".");
    }

    private String getPackageName(String chosenPackage) {
        return chosenPackage.split(".")[chosenPackage.split(".").length - 1];
    }

}
