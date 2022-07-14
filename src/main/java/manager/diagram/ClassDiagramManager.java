package manager.diagram;

import model.LeafNode;
import model.PackageNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassDiagramManager extends DiagramManager{

    public ClassDiagramManager(Map<String, PackageNode> packageNodes) {
        super(packageNodes);
    }

    public void parseChosenFiles(List<String> chosenClassesNames) {
        List<LeafNode> chosenClasses = new ArrayList<>();
        for (String chosenClass: chosenClassesNames) {
            for (PackageNode p: packages.values()){
                if (p.getLeafNodes().containsKey(chosenClass)) {
                    chosenClasses.add(p.getLeafNodes().get(chosenClass));
                }
            }
        }
        graphMLNode.populateGraphMLNodes(chosenClasses);
        graphMLEdge.populateGraphMLEdges(chosenClasses, graphMLNode.getGraphMLNodes());
    }
}
