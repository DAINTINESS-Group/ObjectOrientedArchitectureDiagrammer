package model.diagram.exportation;

import model.diagram.ClassDiagram;
import model.diagram.graphml.GraphMLClassifierVertex;
import model.diagram.graphml.GraphMLClassifierVertexArc;

import java.io.File;
import java.nio.file.Path;

public class GraphMLClassDiagramExporter implements DiagramExporter
{

    private final GraphMLFile   graphMLFile;
    private final StringBuilder graphMLNodeBuffer;
    private final StringBuilder graphMLEdgeBuffer;


    public GraphMLClassDiagramExporter(ClassDiagram classDiagram)
    {
        GraphMLClassifierVertex    graphMLClassifierVertex    = new GraphMLClassifierVertex(classDiagram);
        GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc(classDiagram);

        graphMLNodeBuffer = graphMLClassifierVertex.convertSinkVertex();
        graphMLEdgeBuffer = graphMLClassifierVertexArc.convertSinkVertexArc();
        graphMLFile = new GraphMLFile();
    }


    @Override
    public File exportDiagram(Path exportPath)
    {
        graphMLFile.createGraphMLFile(exportPath);
        graphMLFile.writeToBuffer(graphMLNodeBuffer);
        graphMLFile.writeToBuffer(graphMLEdgeBuffer);
        graphMLFile.closeGraphMLFile();
        return graphMLFile.getGraphMLFile();
    }

}
