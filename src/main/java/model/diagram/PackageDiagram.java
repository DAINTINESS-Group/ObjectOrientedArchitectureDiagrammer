package model.diagram;

import model.tree.node.Node;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageDiagram extends Diagram {

    public List<Node> getChosenNodes(List<String> chosenPackagesNames) {
        List<Node> chosenPackages = new ArrayList<>();
        for (String chosenPackage: chosenPackagesNames) {
            if (!isPackageValid(chosenPackage)) {
                continue;
            }
            chosenPackages.add(sourceProject.getPackageNodes().get(Paths.get(chosenPackage)));
        }
        return chosenPackages;
    }

    private boolean isPackageValid(String chosenPackage) {
        return sourceProject.getPackageNodes().get(Paths.get(chosenPackage)).isValid();
    }

    @Override
    protected StringBuilder convertEdgesToGraphML() {
        return graphEdgeCollection.convertPackageEdgesToGraphML();
    }

    @Override
    protected StringBuilder convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry) {
        return graphNodeCollection.convertPackageNodesToGraphML(nodesGeometry);
    }

}
