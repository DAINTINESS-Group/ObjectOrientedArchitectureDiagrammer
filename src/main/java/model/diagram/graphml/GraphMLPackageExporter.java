package model.diagram.graphml;

import model.graph.Arc;
import model.graph.Vertex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GraphMLPackageExporter {

    private final GraphMLFile graphMLFile;
    private final StringBuilder graphMLNodeBuffer;
    private final StringBuilder graphMLEdgeBuffer;

    public GraphMLPackageExporter(Map<Vertex, Integer> graphNodes, Map<Integer, List<Double>> nodesGeometry, Map<Arc<Vertex>, Integer> graphEdges) {
        GraphMLPackageNode graphMLPackageNode = new GraphMLPackageNode(graphNodes, nodesGeometry);
        graphMLNodeBuffer = graphMLPackageNode.convertPackageNode();
        GraphMLPackageEdge graphMLPackageEdge = new GraphMLPackageEdge(graphNodes);
        graphMLEdgeBuffer = graphMLPackageEdge.convertPackageEdge(graphEdges);

        graphMLFile = new GraphMLFile();
    }

    public File exportDiagramToGraphML(Path graphMLSavePath) {
        try {
            graphMLFile.createGraphMLFile(graphMLSavePath);
            generateGraphMLGraph(graphMLNodeBuffer, graphMLEdgeBuffer);
            graphMLFile.closeGraphMLFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        return graphMLFile.getGraphMLFile();
    }

    private void generateGraphMLGraph(StringBuilder nodeBuffer, StringBuilder edgeBuffer){
        graphMLFile.writeToBuffer(nodeBuffer);
        graphMLFile.writeToBuffer(edgeBuffer);
    }
}
