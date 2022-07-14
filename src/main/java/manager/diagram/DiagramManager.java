package manager.diagram;

import model.PackageNode;

import java.util.*;
import java.util.List;

public abstract class DiagramManager implements GraphMLDiagramManager {

    protected final GraphMLNode graphMLNode;
    protected final GraphMLEdge graphMLEdge;
    private final DiagramArrangement diagramArrangement;
    private final GraphMLExporter graphMLExporter;
    protected final Map<String, PackageNode> packages;

    public DiagramManager (Map<String, PackageNode> packageNodes) {
        diagramArrangement = new DiagramArrangement();
        graphMLExporter = new GraphMLExporter();
        graphMLNode = new GraphMLNode();
        graphMLEdge = new GraphMLEdge();
        this.packages = packageNodes;
    }

    public void createDiagram(List<String> chosenFilesNames) {
        parseChosenFiles(chosenFilesNames);
        diagramArrangement.arrangeDiagram(graphMLNode, graphMLEdge);
        graphMLNode.parseGraphMLNodes(diagramArrangement.getNodesGeometry());
    }

    public void exportDiagramToGraphML(String graphMLSavePath) {
        graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphMLNode.getGraphMLBuffer(), graphMLEdge.getGraphMLBuffer());
    }

    public abstract void parseChosenFiles(List<String> chosenClassesNames);

}