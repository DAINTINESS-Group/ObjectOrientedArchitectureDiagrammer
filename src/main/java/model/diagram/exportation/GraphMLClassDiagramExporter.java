package model.diagram.exportation;

import model.diagram.ClassDiagram;
import model.diagram.graphml.GraphMLClassifierVertex;
import model.diagram.graphml.GraphMLClassifierVertexArc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class GraphMLClassDiagramExporter implements DiagramExporter {

	private final GraphMLFile   graphMLFile;
	private final StringBuilder graphMLNodeBuffer;
	private final StringBuilder graphMLEdgeBuffer;

	public GraphMLClassDiagramExporter(ClassDiagram classDiagram) {
		GraphMLClassifierVertex graphMLClassifierVertex = new GraphMLClassifierVertex(classDiagram);
		this.graphMLNodeBuffer = graphMLClassifierVertex.convertSinkVertex();
		GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc(classDiagram);
		this.graphMLEdgeBuffer = graphMLClassifierVertexArc.convertSinkVertexArc();
		graphMLFile = new GraphMLFile();
	}

	@Override
	public File exportDiagram(Path exportPath) {
		try {
			graphMLFile.createGraphMLFile(exportPath);
			generateGraphMLGraph(graphMLNodeBuffer, graphMLEdgeBuffer);
			graphMLFile.closeGraphMLFile();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return graphMLFile.getGraphMLFile();
	}

	private void generateGraphMLGraph(StringBuilder nodeBuffer, StringBuilder edgeBuffer){
		graphMLFile.writeToBuffer(nodeBuffer);
		graphMLFile.writeToBuffer(edgeBuffer);
	}
}
