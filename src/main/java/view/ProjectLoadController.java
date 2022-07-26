package view;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

public class ProjectLoadController {

    @FXML
    MenuBar menuBar;

    public void loadProject() {
        MenuUtility.loadProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void quitApp() {
        MenuUtility.quitApp(menuBar);
    }

}
