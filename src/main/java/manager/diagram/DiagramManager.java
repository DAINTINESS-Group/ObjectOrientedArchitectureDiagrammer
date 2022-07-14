package manager.diagram;

import model.PackageNode;

import java.io.IOException;
import java.util.*;
import java.util.List;

public abstract class DiagramManager implements GraphMLDiagramManager {

    protected final GraphMLNode graphMLNode;
    protected final GraphMLEdge graphMLEdge;
    private final DiagramArrangement diagramArrangement;
    private final GraphMLFile graphMLFile;
    protected final Map<String, PackageNode> packages;

    public DiagramManager (Map<String, PackageNode> packageNodes) {
        diagramArrangement = new DiagramArrangement();
        graphMLFile = new GraphMLFile();
        graphMLNode = new GraphMLNode();
        graphMLEdge = new GraphMLEdge();
        this.packages = packageNodes;
    }

    public void createDiagram(List<String> chosenFilesNames, String graphMLSavePath) {
        try {
            graphMLFile.createGraphMLFile(graphMLSavePath);
            parseChosenFiles(chosenFilesNames);
            diagramArrangement.arrangeDiagram(graphMLNode, graphMLEdge);
            generateGraphMLGraph();
            graphMLFile.closeGraphMLFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        graphMLFile.printBuffer();
    }

    private void generateGraphMLGraph(){
        graphMLNode.parseGraphMLNodes(diagramArrangement.getNodesGeometry());
        graphMLFile.writeToBuffer(graphMLNode.getGraphMLBuffer());
        graphMLFile.writeToBuffer(graphMLEdge.getGraphMLBuffer());
    }

    public abstract void parseChosenFiles(List<String> chosenClassesNames);

}