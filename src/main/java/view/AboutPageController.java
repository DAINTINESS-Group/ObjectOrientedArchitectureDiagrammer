package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutPageController implements Initializable {

    @FXML
    MenuBar menuBar;
    @FXML
    TextFlow textFlow;

    public void loadDiagram(ActionEvent event) {
        MenuUtility.loadDiagram(menuBar, event);
    }

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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Text text1 = new Text("Sample text");
        textFlow.getChildren().add(text1);
    }
}
