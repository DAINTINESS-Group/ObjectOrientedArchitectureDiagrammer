package gr.uoi.ooad.model.diagram.exportation;

import java.io.File;
import java.nio.file.Path;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.diagram.graphml.GraphMLPackageVertex;
import gr.uoi.ooad.model.diagram.graphml.GraphMLPackageVertexArc;

public class GraphMLPackageDiagramExporter implements DiagramExporter {

    private final GraphMLFile graphMLFile;
    private final StringBuilder graphMLNodeBuffer;
    private final StringBuilder graphMLEdgeBuffer;

    public GraphMLPackageDiagramExporter(PackageDiagram packageDiagram) {
        GraphMLPackageVertex graphMLPackageVertex = new GraphMLPackageVertex();
        GraphMLPackageVertexArc graphMLPackageVertexArc = new GraphMLPackageVertexArc();

        graphMLNodeBuffer = graphMLPackageVertex.convertVertex(packageDiagram);
        graphMLEdgeBuffer = graphMLPackageVertexArc.convertVertexArc(packageDiagram);
        graphMLFile = new GraphMLFile();
    }

    @Override
    public File exportDiagram(Path exportPath) {
        graphMLFile.createGraphMLFile(exportPath);
        generateGraphMLGraph(graphMLNodeBuffer, graphMLEdgeBuffer);
        graphMLFile.closeGraphMLFile();
        return graphMLFile.getGraphMLFile();
    }

    private void generateGraphMLGraph(StringBuilder nodeBuffer, StringBuilder edgeBuffer) {
        graphMLFile.writeToBuffer(nodeBuffer);
        graphMLFile.writeToBuffer(edgeBuffer);
    }
}
