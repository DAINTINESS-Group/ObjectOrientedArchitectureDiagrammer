package model.diagram;

import model.diagram.graphml.GraphMLPackageEdge;
import model.diagram.graphml.GraphMLPackageNode;
import model.tree.Node;

import java.nio.file.Paths;
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
                chosenPackages.add(sourceProject.getPackageNodes().get(Paths.get(chosenPackage)));
            }
        }
        return chosenPackages;
    }

    private boolean isPackageValid(String chosenPackage) {
        return sourceProject.getPackageNodes().get(Paths.get(chosenPackage)).isValid();
    }

}
