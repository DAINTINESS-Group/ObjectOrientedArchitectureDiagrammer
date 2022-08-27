package model;

import manager.ClassDiagramManager;
import manager.DiagramManager;
import model.diagram.javafx.JavaFXExporter;
import model.tree.SourceProject;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaFXExporterTest {

    Path currentDirectory = Path.of(".");

    @Test
    void saveDiagramTest() throws IOException {
        DiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
        SourceProject sourceProject = classDiagramManager.createTree(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        Map<String, Map<String, String>> createdDiagram = classDiagramManager.createDiagram(chosenFiles);

        JavaFXExporter javaFXExporter = new JavaFXExporter();
        File actualFile = javaFXExporter.saveDiagram(createdDiagram, Paths.get(System.getProperty("user.home")+"\\testingExportedFile.graphML"));

        Properties propertiesMap = new Properties();
        for (Map.Entry<String, Map<String, String>> entry: createdDiagram.entrySet()) {
            Properties propertiesValues = new Properties();
            propertiesValues.putAll(entry.getValue());
            propertiesMap.put(entry.getKey(), propertiesValues.toString());
        }

        try {
            File expectedFile = new File(System.getProperty("user.home")+"\\testingExportedFile.graphML");
            propertiesMap.store(new FileOutputStream(expectedFile), null);
            assertTrue(FileUtils.contentEquals(expectedFile, actualFile));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
