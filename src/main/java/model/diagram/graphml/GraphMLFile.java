package model.diagram.graphml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GraphMLFile {

    private FileWriter graphMLWriter;
    private StringBuilder graphMLBuffer;
    private File graphMLFile;

    public void createGraphMLFile(String graphMLSavePath) throws IOException {
        graphMLFile = new File(graphMLSavePath);
        graphMLWriter = new FileWriter(graphMLSavePath);
        graphMLBuffer = new StringBuilder();
        graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPrefix());
    }


    public void closeGraphMLFile() throws IOException {
        graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLSuffix());
        graphMLWriter.write(graphMLBuffer.toString());
        graphMLWriter.close();
    }

    public void writeToBuffer(String buffer) {
        graphMLBuffer.append(buffer);
    }

    public File getGraphMLFile() {
        return graphMLFile;
    }

}
