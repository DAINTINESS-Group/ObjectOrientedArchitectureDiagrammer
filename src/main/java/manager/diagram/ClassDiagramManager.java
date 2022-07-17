package manager.diagram;

import model.LeafNode;
import model.PackageNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassDiagramManager extends DiagramManager{

    public ClassDiagramManager(Map<String, PackageNode> packageNodes) {
        super(packageNodes);
    }

    public void createDiagram(List<String> chosenClassesNames) {
        List<LeafNode> chosenClasses = new ArrayList<>();
        for (String chosenClass: chosenClassesNames) {
            for (PackageNode p: packages.values()){
                if (p.getLeafNodes().containsKey(chosenClass)) {
                    chosenClasses.add(p.getLeafNodes().get(chosenClass));
                    break;
                }
            }
        }
        graphMLNode.populateGraphMLNodes(chosenClasses);
        graphMLEdge.populateGraphMLEdges(chosenClasses, graphMLNode.getGraphMLNodes());
    }

    public void arrangeDiagram() {
        DiagramArrangement diagramArrangement = new ClassDiagramArrangement();
        diagramArrangement.arrangeDiagram(getCollect(), getNewMap2());
        nodesGeometry = diagramArrangement.getNodesGeometry();
    }

    private Map<Object, Integer> getNewMap2() {
        return graphMLEdge.getGraphMLEdges().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<Object, Integer> getCollect() {
        return graphMLNode.getGraphMLNodes().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void exportDiagramToGraphML(String graphMLSavePath) {
        graphMLNode.convertNodesToGraphML(nodesGeometry);
        graphMLEdge.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphMLNode.getGraphMLBuffer(), graphMLEdge.getGraphMLBuffer());
    }
}
