package manager.diagram;

import model.PackageNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageDiagramManager extends DiagramManager{

    public PackageDiagramManager(Map<String, PackageNode> packageNodes) {
        super(packageNodes);
    }

    public void parseChosenFiles(List<String> chosenPackagesNames) {
        for (String packageName: chosenPackagesNames) {
            createGraphMLNodes(packageName);
            createGraphMLEdges(packageName);
        }

    }
    private void createGraphMLNodes(String packageName) {
        graphMLNode.populateGraphMLNodes(new ArrayList<>(packages.get(packageName).getLeafNodes().values()));
        createSubPackagesGraphMLNodes(packageName);
    }

    private void createSubPackagesGraphMLNodes(String chosenPackagePath) {
        for (PackageNode p: packages.get(chosenPackagePath).getSubNodes().values()) {
            if (p.isValid()) {
                graphMLNode.populateGraphMLNodes(new ArrayList<>(p.getLeafNodes().values()));
            }
            createSubPackagesGraphMLNodes(p.getName());
        }
    }

    private void createGraphMLEdges(String packageName) {
        graphMLEdge.populateGraphMLEdges(new ArrayList<>(packages.get(packageName).getLeafNodes().values()), graphMLNode.getGraphMLNodes());
        createSubPackagesGraphMLEdges(packageName);
    }

    private void createSubPackagesGraphMLEdges(String packageName) {
        for (PackageNode p : packages.get(packageName).getSubNodes().values()) {
            if (p.isValid()) {
                graphMLEdge.populateGraphMLEdges(new ArrayList<>(p.getLeafNodes().values()), graphMLNode.getGraphMLNodes());
            }
            createSubPackagesGraphMLEdges(p.getName());
        }
    }
}
