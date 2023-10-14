package model.diagram.exportation;

import model.diagram.PackageDiagram;
import model.diagram.graphml.GraphMLPackageVertex;
import model.diagram.graphml.GraphMLPackageVertexArc;

import java.io.File;
import java.nio.file.Path;

public class GraphMLPackageDiagramExporter implements DiagramExporter {

	private final GraphMLFile   graphMLFile;
	private final StringBuilder graphMLNodeBuffer;
	private final StringBuilder graphMLEdgeBuffer;

	public GraphMLPackageDiagramExporter(PackageDiagram packageDiagram) {
		GraphMLPackageVertex graphMLPackageVertex 		= new GraphMLPackageVertex(packageDiagram);
		this.graphMLNodeBuffer 						  	= graphMLPackageVertex.convertVertex();
		GraphMLPackageVertexArc graphMLPackageVertexArc = new GraphMLPackageVertexArc(packageDiagram);
		this.graphMLEdgeBuffer 							= graphMLPackageVertexArc.convertVertexArc();
		this.graphMLFile 								= new GraphMLFile();
	}

	@Override
	public File exportDiagram(Path exportPath) {
		this.graphMLFile.createGraphMLFile(exportPath);
		generateGraphMLGraph(this.graphMLNodeBuffer, this.graphMLEdgeBuffer);
		this.graphMLFile.closeGraphMLFile();
		return this.graphMLFile.getGraphMLFile();
	}

	private void generateGraphMLGraph(StringBuilder nodeBuffer,
									  StringBuilder edgeBuffer) {
		this.graphMLFile.writeToBuffer(nodeBuffer);
		this.graphMLFile.writeToBuffer(edgeBuffer);
	}
}
