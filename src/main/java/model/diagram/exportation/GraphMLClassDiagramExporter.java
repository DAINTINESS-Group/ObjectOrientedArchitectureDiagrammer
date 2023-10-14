package model.diagram.exportation;

import model.diagram.ClassDiagram;
import model.diagram.graphml.GraphMLClassifierVertex;
import model.diagram.graphml.GraphMLClassifierVertexArc;

import java.io.File;
import java.nio.file.Path;

public class GraphMLClassDiagramExporter implements DiagramExporter {

	private final GraphMLFile   graphMLFile;
	private final StringBuilder graphMLNodeBuffer;
	private final StringBuilder graphMLEdgeBuffer;

	public GraphMLClassDiagramExporter(ClassDiagram classDiagram) {
		GraphMLClassifierVertex graphMLClassifierVertex 	  = new GraphMLClassifierVertex(classDiagram);
		this.graphMLNodeBuffer 								  = graphMLClassifierVertex.convertSinkVertex();
		GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc(classDiagram);
		this.graphMLEdgeBuffer 								  = graphMLClassifierVertexArc.convertSinkVertexArc();
		this.graphMLFile 									  = new GraphMLFile();
	}

	@Override
	public File exportDiagram(Path exportPath) {
		this.graphMLFile.createGraphMLFile(exportPath);
		this.graphMLFile.writeToBuffer(this.graphMLNodeBuffer);
		this.graphMLFile.writeToBuffer(this.graphMLEdgeBuffer);
		this.graphMLFile.closeGraphMLFile();
		return this.graphMLFile.getGraphMLFile();
	}

}
