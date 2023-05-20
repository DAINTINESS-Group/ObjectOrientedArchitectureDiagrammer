package model.diagram;

import model.tree.node.Node;
import model.tree.node.PackageNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    protected StringBuilder convertEdgesToGraphML() {
        return graphEdgeCollection.convertLeafEdgesToGraphML();
    }

    @Override
    protected StringBuilder convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry) {
        return graphNodeCollection.convertLeafNodesToGraphML(nodesGeometry);
    }

}
