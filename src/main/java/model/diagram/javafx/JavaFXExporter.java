package model.diagram.javafx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JavaFXExporter {

    private static final int EDGE_ENDING_NODE = 0;
    private static final int EDGE_TYPE = 1;

    public File saveDiagram(Map<String, Map<String, String>> createdDiagram, String graphSavePath){
        return createFile(graphSavePath, populateProperties(createdDiagram));
    }

    private Properties populateProperties(Map<String, Map<String, String>> createdDiagram) {
        Properties propertiesMap = new Properties();
        for (Map.Entry<String, Map<String, String>> entry: createdDiagram.entrySet()) {
            Properties propertiesValues = new Properties();
            propertiesValues.putAll(entry.getValue());
            propertiesMap.put(entry.getKey(), propertiesValues.toString());
        }
        return propertiesMap;
    }

    private File createFile(String graphSavePath, Properties propertiesMap) {
        try {
            File graphSaveFile = new File(graphSavePath);
            propertiesMap.store(new FileOutputStream(graphSaveFile), null);
            return graphSaveFile;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return new File("");
    }

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
