package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.nio.file.Path;

public class DiagramCreationController {

    @FXML
    MenuBar menuBar;
    @FXML
    TreeView<String> treeView;
    @FXML
    HBox hBox;

    private ProjectTreeView projectTreeView;
    private DiagramCreation diagramCreation;

    public void createTreeView(Path sourceFolderPath){
        projectTreeView = new ProjectTreeView(treeView, sourceFolderPath);
        projectTreeView.createTreeView();
    }

    public void createProject(ActionEvent event) {
        diagramCreation = new DiagramCreation(projectTreeView, menuBar);
        diagramCreation.createProject(((MenuItem) event.getSource()).getText());
    }

    public void loadProject() {
        diagramCreation.loadProject();
    }

    public void viewProject(ActionEvent event) {
        diagramCreation.viewProject(((MenuItem) event.getSource()).getText());
    }

    public void openProject() {
        MenuUtility.openProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void loadDiagram(ActionEvent event) {
        MenuUtility.loadDiagram(menuBar, event);
    }

    public void aboutPage() { MenuUtility.aboutPage(menuBar); }

    public void quitApp() { MenuUtility.quitApp(menuBar); }

}
