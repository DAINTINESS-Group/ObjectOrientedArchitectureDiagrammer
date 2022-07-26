package view;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class FolderChooser {

    @FXML
    MenuBar menuBar;

    private final File selectedDirectory;

    public FolderChooser(String title, MenuBar menuBar) {
        this.menuBar = menuBar;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        directoryChooser.setTitle(title);
        Stage window = (Stage) menuBar.getScene().getWindow();
        this.selectedDirectory = directoryChooser.showDialog(window);
    }

    public File getSelectedDirectory() {
        return selectedDirectory;
    }
}
