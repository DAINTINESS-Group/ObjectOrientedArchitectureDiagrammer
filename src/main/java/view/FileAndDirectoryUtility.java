package view;

import javafx.scene.control.MenuBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;

import static java.util.Map.entry;

public class FileAndDirectoryUtility {

    private FileAndDirectoryUtility() { throw new java.lang.UnsupportedOperationException("Not to be instantiated"); }

    public static File chooseDirectory(String windowTitle, MenuBar menuBar) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        //directoryChooser.setInitialDirectory(new File("C:\\Users\\user\\IntelliJProjects\\UMLDiagramTool\\src\\test\\resources\\LatexEditor"));
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        directoryChooser.setTitle(windowTitle);
        Stage window = (Stage) menuBar.getScene().getWindow();
        return directoryChooser.showDialog(window);
    }

    public static File saveFile(String windowTitle, MenuBar menuBar, String fileType) {
        final Map<String, String> availableExtensionTypes = Map.ofEntries(
                entry("Text Files", "*.txt"),
                entry("GraphML Files", "*.graphML"),
                entry("PNG files", "*.png"),
        		entry("PlantUML Files", "*.png"),
        		entry("PlantUML Text Files", "*.txt"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(windowTitle);
        //fileChooser.setInitialDirectory(new File("C:\\Users\\user\\IntelliJProjects\\UMLDiagramTool\\src\\test\\resources\\LatexEditor"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType, availableExtensionTypes.get(fileType)));
        if (fileType == "PlantUML Files" || fileType == "PlantUML Text Files") {
        	fileChooser.setInitialFileName(String.format("plantUML%s", availableExtensionTypes.get(fileType).substring(1)));
        }else {
        	fileChooser.setInitialFileName(String.format("createdDiagram%s", availableExtensionTypes.get(fileType).substring(1)));
        }
        Stage window = (Stage) menuBar.getScene().getWindow();
        return fileChooser.showSaveDialog(window);
    }

    public static File loadFile(String windowTitle, MenuBar menuBar) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(windowTitle);
        //fileChooser.setInitialDirectory(new File("C:\\Users\\user\\IntelliJProjects\\UMLDiagramTool\\src\\test\\resources\\LatexEditor"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        fileChooser.setInitialFileName(String.format("createdDiagram%s", ".txt"));
        Stage window = (Stage) menuBar.getScene().getWindow();
        return fileChooser.showOpenDialog(window);
    }

}
