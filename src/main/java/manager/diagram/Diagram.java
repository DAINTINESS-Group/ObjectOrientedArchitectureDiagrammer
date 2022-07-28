package manager.diagram;

import model.diagram.DiagramArrangement;
import model.diagram.GraphEdgeCollection;
import model.diagram.GraphNodeCollection;
import model.diagram.graphml.GraphMLExporter;
import model.tree.Node;
import model.tree.SourceProject;

import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class Diagram {

    protected Map<Integer, List<Double>> nodesGeometry;
    protected GraphNodeCollection graphNodeCollection;
    protected GraphEdgeCollection graphEdgeCollection;
    protected final SourceProject sourceProject;

    public Diagram(SourceProject sourceProject) {
        this.sourceProject = sourceProject;
    }

    public Map<String, Map<String, String>> createDiagram(List<String> chosenFilesNames){
        graphNodeCollection.populateGraphNodes(getChosenNodes(chosenFilesNames));
        graphEdgeCollection.setGraphNodes(graphNodeCollection.getGraphNodes());
        graphEdgeCollection.populateGraphEdges(getChosenNodes(chosenFilesNames));
        return convertCollectionsToDiagram();
    }

    public Map<Integer, List<Double>> arrangeDiagram(){
        DiagramArrangement diagramArrangement = new DiagramArrangement();
        nodesGeometry = diagramArrangement.arrangeDiagram(graphNodeCollection.getGraphNodes(), graphEdgeCollection.getGraphEdges());
        return nodesGeometry;
    }

    public File exportDiagramToGraphML(String graphMLSavePath){
        graphNodeCollection.convertNodesToGraphML(nodesGeometry);
        graphEdgeCollection.convertEdgesToGraphML();
        GraphMLExporter graphMLExporter = new GraphMLExporter();
        return graphMLExporter.exportDiagramToGraphML(graphMLSavePath, graphNodeCollection.getGraphMLBuffer(), graphEdgeCollection.getGraphMLBuffer());
    }

    public abstract Map<String, Map<String, String>> convertCollectionsToDiagram();

    public abstract List<Node> getChosenNodes(List<String> chosenFileNames);
}
