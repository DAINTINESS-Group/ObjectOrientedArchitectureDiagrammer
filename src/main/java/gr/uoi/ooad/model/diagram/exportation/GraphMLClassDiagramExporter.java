package gr.uoi.ooad.model.diagram.exportation;

import java.io.File;
import java.nio.file.Path;
import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.diagram.graphml.GraphMLClassifierVertex;
import gr.uoi.ooad.model.diagram.graphml.GraphMLClassifierVertexArc;

public class GraphMLClassDiagramExporter implements DiagramExporter {

    private final GraphMLFile graphMLFile;
    private final StringBuilder graphMLNodeBuffer;
    private final StringBuilder graphMLEdgeBuffer;

    public GraphMLClassDiagramExporter(ClassDiagram classDiagram) {
        GraphMLClassifierVertex graphMLClassifierVertex = new GraphMLClassifierVertex();
        GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc();

        graphMLNodeBuffer = graphMLClassifierVertex.convertSinkVertex(classDiagram);
        graphMLEdgeBuffer = graphMLClassifierVertexArc.convertSinkVertexArc(classDiagram);
        graphMLFile = new GraphMLFile();
    }

    @Override
    public File exportDiagram(Path exportPath) {
        graphMLFile.createGraphMLFile(exportPath);
        graphMLFile.writeToBuffer(graphMLNodeBuffer);
        graphMLFile.writeToBuffer(graphMLEdgeBuffer);
        graphMLFile.closeGraphMLFile();
        return graphMLFile.getGraphMLFile();
    }
}
