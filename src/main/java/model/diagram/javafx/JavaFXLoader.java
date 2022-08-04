package model.diagram.javafx;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JavaFXLoader {

    private static final int EDGE_ENDING_NODE = 0;
    private static final int EDGE_TYPE = 1;

    public Map<String, Map<String, String>> loadDiagram(String graphSavePath){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(graphSavePath));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return populateDiagram(properties);
    }

    private Map<String, Map<String, String>> populateDiagram(Properties properties) {
        Map<String, Map<String, String>> loadedDiagram = new HashMap<>();
        for (String node : properties.stringPropertyNames()) {
            Map<String, String> nodesEdges = new HashMap<>();
            for (String nodesEdge: properties.get(node).toString().substring(1, properties.get(node).toString().length()-1).split(",")) {
                if (nodesEdge.isEmpty()) {
                    continue;
                }
                nodesEdges.put(nodesEdge.split("=")[EDGE_ENDING_NODE].trim(), nodesEdge.split("=")[EDGE_TYPE].trim());
            }
            loadedDiagram.put(node, nodesEdges);
        }
        return loadedDiagram;
    }

}
