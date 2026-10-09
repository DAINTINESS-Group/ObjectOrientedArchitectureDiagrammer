package gr.uoi.ooad.model.diagram.exportation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import gr.uoi.ooad.model.diagram.graphml.GraphMLSyntax;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GraphMLFile {
    private static final Logger logger = LogManager.getLogger(GraphMLFile.class);

    private FileWriter graphMLWriter;
    private StringBuilder graphMLBuffer;
    private File graphMLFile;

    public void createGraphMLFile(Path graphMLSavePath) {
        try {
            graphMLFile = graphMLSavePath.toFile();
            graphMLWriter = new FileWriter(graphMLFile);
            graphMLBuffer = new StringBuilder();
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPrefix());
        } catch (IOException e) {
            logger.error("Can't write to file: {}", graphMLFile.getAbsoluteFile());
            throw new RuntimeException(e);
        }
    }

    public void closeGraphMLFile() {
        try {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLSuffix());
            graphMLWriter.write(graphMLBuffer.toString());
            graphMLWriter.close();
        } catch (IOException e) {
            logger.error("Can't write buffer to file: {}", graphMLFile.getAbsoluteFile());
            throw new RuntimeException(e);
        }
    }

    public void writeToBuffer(StringBuilder buffer) {
        graphMLBuffer.append(buffer);
    }

    public File getGraphMLFile() {
        return graphMLFile;
    }
}
