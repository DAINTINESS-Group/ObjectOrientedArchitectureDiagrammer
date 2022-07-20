package manager.diagram;

import java.io.IOException;

public class GraphMLExporter {

    private final GraphMLFile graphMLFile;

    public GraphMLExporter() {
        graphMLFile = new GraphMLFile();
    }

    public void exportDiagramToGraphML(String graphMLSavePath, String nodeBuffer, String edgeBuffer) {
        try {
            graphMLFile.createGraphMLFile(graphMLSavePath);
            generateGraphMLGraph(nodeBuffer, edgeBuffer);
            graphMLFile.closeGraphMLFile();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void generateGraphMLGraph(String nodeBuffer, String edgeBuffer){
        graphMLFile.writeToBuffer(nodeBuffer);
        graphMLFile.writeToBuffer(edgeBuffer);
    }
}
