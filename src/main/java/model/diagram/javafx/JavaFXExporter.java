package model.diagram.javafx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class JavaFXExporter {

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

}
