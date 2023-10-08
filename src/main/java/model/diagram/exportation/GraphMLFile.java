package model.diagram.exportation;

import model.diagram.graphml.GraphMLSyntax;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class GraphMLFile {

	private FileWriter    graphMLWriter;
	private StringBuilder graphMLBuffer;
	private File          graphMLFile;

	public void createGraphMLFile(Path graphMLSavePath) throws IOException {
		graphMLFile   = graphMLSavePath.toFile();
		graphMLWriter = new FileWriter(graphMLFile);
		graphMLBuffer = new StringBuilder();
		graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPrefix());
	}


	public void closeGraphMLFile() throws IOException {
		graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLSuffix());
		graphMLWriter.write(graphMLBuffer.toString());
		graphMLWriter.close();
	}

	public void writeToBuffer(StringBuilder buffer) {
		graphMLBuffer.append(buffer);
	}

	public File getGraphMLFile() {
		return graphMLFile;
	}

}
