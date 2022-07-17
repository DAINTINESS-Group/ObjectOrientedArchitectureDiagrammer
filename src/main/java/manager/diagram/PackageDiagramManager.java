package manager.diagram;

import model.PackageNode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PackageDiagramManager extends DiagramManager{

    public PackageDiagramManager(Map<String, PackageNode> packageNodes) {
        super(packageNodes);
    }

    public void createDiagram(List<String> chosenPackagesNames) {
        for (String packageName: chosenPackagesNames) {
            createGraphMLNodes(packageName);
            createGraphMLEdges(packageName);
        }
    }

    private void createGraphMLNodes(String packageName) {
        graphMLPackageNode.populateGraphMLNodes(packages.get(packageName));
        createSubPackagesGraphMLNodes(packageName);
    }

    private void createSubPackagesGraphMLNodes(String chosenPackagePath) {
        for (PackageNode p: packages.get(chosenPackagePath).getSubNodes().values()) {
            if (p.isValid()) {
                graphMLPackageNode.populateGraphMLNodes(p);
            }
            createSubPackagesGraphMLNodes(p.getName());
        }
    }

    private void createGraphMLEdges(String packageName) {
        graphMLPackageEdge.populateGraphMLEdges(packages.get(packageName), graphMLPackageNode.getGraphMLNodes());
        createSubPackagesGraphMLEdges(packageName);
    }

    private void createSubPackagesGraphMLEdges(String packageName) {
        for (PackageNode p : packages.get(packageName).getSubNodes().values()) {
            if (p.isValid()) {
                graphMLPackageEdge.populateGraphMLEdges(p, graphMLPackageNode.getGraphMLNodes());
            }
            createSubPackagesGraphMLEdges(p.getName());
        }
    }

    public void arrangeDiagram() {
        DiagramArrangement diagramArrangement = new PackageDiagramArrangement();
        diagramArrangement.arrangeDiagram(graphMLPackageNode.getGraphMLNodes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                graphMLPackageEdge.getGraphMLEdges().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        nodesGeometry = diagramArrangement.getNodesGeometry();
    }

    public void exportDiagramToGraphML(String graphMLSavePath) {
        graphMLPackageNode.convertNodesToGraphML(nodesGeometry);
        graphMLPackageEdge.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphMLPackageNode.getGraphMLBuffer(), graphMLPackageEdge.getGraphMLBuffer());
    }

}
