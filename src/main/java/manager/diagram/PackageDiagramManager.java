package manager.diagram;

import model.diagram.*;
import model.tree.PackageNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageDiagramManager extends DiagramManager{

    private final GraphMLNode<PackageNode> graphMLPackageNode;
    private final GraphMLPackageEdge graphMLPackageEdge;

    public PackageDiagramManager(Map<String, PackageNode> packageNodes) {
        super(packageNodes);
        graphMLPackageNode = new GraphMLPackageNode<>();
        graphMLPackageEdge = new GraphMLPackageEdge();
    }

    public void createDiagram(List<String> chosenPackagesNames) {
        graphMLPackageNode.populateGraphMLNodes(getChosenPackages(chosenPackagesNames));
        graphMLPackageEdge.setGraphMLNodes(graphMLPackageNode.getGraphMLNodes());
        graphMLPackageEdge.populateGraphMLEdges(getChosenPackages(chosenPackagesNames));
    }

    private List<PackageNode> getChosenPackages(List<String> chosenPackagesNames) {
        List<PackageNode> chosenPackages = new ArrayList<>();
        for (String chosenPackage: chosenPackagesNames) {
            if (packages.get(chosenPackage).isValid()) {
                chosenPackages.add(packages.get(chosenPackage));
            }
        }
        return chosenPackages;
    }

    public void arrangeDiagram() {
        DiagramArrangement<PackageNode> diagramArrangement = new PackageDiagramArrangement<>();
        diagramArrangement.arrangeDiagram(graphMLPackageNode.getGraphMLNodes(), graphMLPackageEdge.getGraphMLEdges());
        nodesGeometry = diagramArrangement.getNodesGeometry();
    }

    public void exportDiagramToGraphML(String graphMLSavePath) {
        graphMLPackageNode.convertNodesToGraphML(nodesGeometry);
        graphMLPackageEdge.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphMLPackageNode.getGraphMLBuffer(), graphMLPackageEdge.getGraphMLBuffer());
    }

}
