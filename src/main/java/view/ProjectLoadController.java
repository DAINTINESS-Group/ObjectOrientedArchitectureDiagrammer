package view;

import javafx.event.ActionEvent;
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

    public void aboutPage() { MenuUtility.aboutPage(menuBar); }

    public void saveDiagram() {
        PopupWindow.createPopupInfoWindow("You haven't created a diagram yet!", "Error");
    }

    public void loadDiagram(ActionEvent event) {
        MenuUtility.loadDiagram(menuBar, event);
    }

}
