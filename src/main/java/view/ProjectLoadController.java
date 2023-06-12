package view;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

public class ProjectLoadController {

    @FXML
    MenuBar menuBar;
    @FXML
    BorderPane borderPane;

    public void openProject() {
        MenuUtility.openProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void quitApp() {
        MenuUtility.quitApp(menuBar);
    }

    public void aboutPage() { MenuUtility.aboutPage(menuBar); }

    public void loadDiagram(ActionEvent event) {
        MenuUtility.loadDiagram(menuBar, event);
    }

    public void visualizeGraph(SmartGraphPanel<String, String> graphView) {
        borderPane.setCenter(new ContentZoomPane(graphView));
    }

    public void closeDiagram() {
        MenuUtility.closeProject(menuBar);
    }
}
