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

	public void createGraphMLFile(Path graphMLSavePath) {
		try {
			this.graphMLFile   = graphMLSavePath.toFile();
			this.graphMLWriter = new FileWriter(this.graphMLFile);
			this.graphMLBuffer = new StringBuilder();
			this.graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPrefix());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void closeGraphMLFile() {
		try {
			this.graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLSuffix());
			this.graphMLWriter.write(this.graphMLBuffer.toString());
			this.graphMLWriter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToBuffer(StringBuilder buffer) {
		this.graphMLBuffer.append(buffer);
	}

	public File getGraphMLFile() {
		return this.graphMLFile;
	}

}
