package model.diagram.graphml;

import model.diagram.DiagramExporter;
import model.graph.Arc;
import model.graph.SinkVertex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GraphMLClassDiagramExporter implements DiagramExporter {

    private final GraphMLFile graphMLFile;
    private final StringBuilder graphMLNodeBuffer;
    private final StringBuilder graphMLEdgeBuffer;

    public GraphMLClassDiagramExporter(Map<SinkVertex, Integer> graphNodes, Map<Integer, List<Double>> nodesGeometry, Map<Arc<SinkVertex>, Integer> graphEdges) {
        GraphMLSinkVertex graphMLSinkVertex = new GraphMLSinkVertex(graphNodes, nodesGeometry);
        this.graphMLNodeBuffer = graphMLSinkVertex.convertLeafNode();
        GraphMLSinkVertexArc graphMLSinkVertexArc = new GraphMLSinkVertexArc(graphNodes);
        this.graphMLEdgeBuffer = graphMLSinkVertexArc.convertLeafEdge(graphEdges);
        graphMLFile = new GraphMLFile();
    }

    @Override
    public File exportDiagram(Path exportPath) {
        try {
            graphMLFile.createGraphMLFile(exportPath);
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
