package model.diagram.exportation;

import model.diagram.PackageDiagram;
import model.diagram.graphml.GraphMLVertex;
import model.diagram.graphml.GraphMLVertexArc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class GraphMLPackageDiagramExporter implements DiagramExporter {

    private final GraphMLFile graphMLFile;
    private final StringBuilder graphMLNodeBuffer;
    private final StringBuilder graphMLEdgeBuffer;

    public GraphMLPackageDiagramExporter(PackageDiagram packageDiagram) {
        GraphMLVertex graphMLVertex = new GraphMLVertex(packageDiagram);
        graphMLNodeBuffer = graphMLVertex.convertVertex();
        GraphMLVertexArc graphMLVertexArc = new GraphMLVertexArc(packageDiagram);
        graphMLEdgeBuffer = graphMLVertexArc.convertVertexArc();
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
