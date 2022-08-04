package view;

import controller.Controller;
import controller.DiagramControllerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.File;

public class DiagramCreationController {

    private static final int DIAGRAM_TYPE = 0;

    @FXML
    MenuBar menuBar;
    @FXML
    TreeView treeView;
    @FXML
    HBox hBox;

    private ProjectTreeView projectTreeView;

    public void chooseDiagramVisualization(ActionEvent event) {
        projectTreeView.setCheckedItems(projectTreeView.getRootItem());
        PopupWindow popupWindow = new PopupWindow(menuBar);
        if (!wereFilesChosen()) {
            popupWindow.createPopupInfoWindow("No files were selected!", "Error");
        }else {
            popupWindow.createDiagramPopupWindow(((Button) event.getSource()).getText(), projectTreeView);
        }
    }

    private boolean wereFilesChosen() {
        return !(projectTreeView.getSelectedFiles(projectTreeView.getFolderFiles()).size() == 0 &&
                projectTreeView.getSelectedFiles(projectTreeView.getJavaSourceFiles()).size() == 0);
    }

    public void createTreeView(String sourceFolderPath){
        projectTreeView = new ProjectTreeView(treeView, sourceFolderPath);
        projectTreeView.createTreeView();
    }

    public void newProject() {
        MenuUtility.newProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void loadDiagram(ActionEvent event) {
        DiagramControllerFactory diagramControllerFactory = new DiagramControllerFactory();
        Controller diagramController = diagramControllerFactory.getDiagramController(((MenuItem) event.getSource()).getText().split(" ")[DIAGRAM_TYPE]);
        File selectedFile = FileAndDirectoryUtility.loadFile(String.format("Load %s", ((MenuItem) event.getSource()).getText()), menuBar);
        if (selectedFile != null) {
            DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
            diagramVisualization.setDiagramController(diagramController);
            diagramController.loadDiagram(selectedFile.getPath());
            diagramVisualization.loadDiagramVisualization(diagramController.visualizeJavaFXGraph());
        }
    }

    public void aboutPage() { MenuUtility.aboutPage(menuBar); }

    public void quitApp() { MenuUtility.quitApp(menuBar); }

}
