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


    public void newProject() {
        MenuUtility.newProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void quitApp() {
        MenuUtility.quitApp(menuBar);
    }

    public void chooseDiagramVisualization(ActionEvent event) {
        projectTreeView.findCheckedItems(projectTreeView.getRootItem());
        PopupWindow popupWindow = new PopupWindow(menuBar, projectTreeView);
        if (!wereFilesChosen()) {
            popupWindow.createPopupErrorWindow();
        }else {
            popupWindow.createPopupWindow(((Button) event.getSource()).getText());
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

}
