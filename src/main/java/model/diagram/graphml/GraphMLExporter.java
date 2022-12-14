package model.diagram.graphml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class GraphMLExporter {

    private final GraphMLFile graphMLFile;

    public GraphMLExporter() {
        graphMLFile = new GraphMLFile();
    }

    public File exportDiagramToGraphML(Path graphMLSavePath, String nodeBuffer, String edgeBuffer) {
        try {
            graphMLFile.createGraphMLFile(graphMLSavePath);
            generateGraphMLGraph(nodeBuffer, edgeBuffer);
            graphMLFile.closeGraphMLFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        return graphMLFile.getGraphMLFile();
    }

    private void generateGraphMLGraph(String nodeBuffer, String edgeBuffer){
        graphMLFile.writeToBuffer(nodeBuffer);
        graphMLFile.writeToBuffer(edgeBuffer);
    }

}
