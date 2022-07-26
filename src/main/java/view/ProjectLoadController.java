package view;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

public class ProjectLoadController {

    @FXML
    MenuBar menuBar;

    public void newProject() {
        MenuUtility.newProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void quitApp() {
        MenuUtility.quitApp(menuBar);
    }

}
