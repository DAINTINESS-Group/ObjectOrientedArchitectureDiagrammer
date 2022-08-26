package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class DiagramCreationController {

    @FXML
    MenuBar menuBar;
    @FXML
    TreeView treeView;
    @FXML
    HBox hBox;

    private ProjectTreeView projectTreeView;
    private DiagramCreation diagramCreation;

    public void createTreeView(String sourceFolderPath){
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

    public void newProject() {
        MenuUtility.newProject(menuBar);
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
