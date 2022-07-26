package view;

import controller.DiagramController;
import controller.DiagramControllerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class DiagramCreationController {

    @FXML
    MenuBar menuBar;
    @FXML
    TreeView treeView;
    @FXML
    HBox hBox;

    private ProjectTreeView projectTreeView;


    public void loadProject() {
        MenuUtility.loadProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void quitApp() {
        MenuUtility.quitApp(menuBar);
    }

    public void chooseDiagramVisualization(ActionEvent event) {
        projectTreeView.findCheckedItems(projectTreeView.getRootItem());
        if (!wereFilesChosen()) {
            PopupWindow popupWindow = new PopupWindow();
            popupWindow.createPopupErrorWindow();
        }else {
            createPopupWindow(((Button) event.getSource()).getText());
        }
    }

    private boolean wereFilesChosen() {
        return !(projectTreeView.getSelectedFiles(projectTreeView.getFolderFiles()).size() == 0 &&
                projectTreeView.getSelectedFiles(projectTreeView.getJavaSourceFiles()).size() == 0);
    }

    private void createPopupWindow(String buttonsText) {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Choose how to view diagram");
        Label label1= new Label("Export as GraphML or visualize here");
        
        Button exportButton = new Button("Export as GraphML");
        exportButton.setOnAction(e -> {
            chooseFiles(buttonsText, "Export");
            popupWindow.close();
        });
        
        Button visualizeButton = new Button("Visualize here");
        visualizeButton.setOnAction(e -> {
            chooseFiles(buttonsText, "View");
            popupWindow.close();
        });
        
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, exportButton, visualizeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 400, 250);
        scene1.getStylesheets().add("styles/modena_dark.css");
        popupWindow.setScene(scene1);
        popupWindow.showAndWait();
    }

    private void chooseFiles(String fileType, String visualizationType) {
        if (fileType.equals("Package Diagram")) {
            createDiagram(visualizationType, "Package");
        }else {
            createDiagram(visualizationType, "Class");
        }
    }

    private void createDiagram(String visualizationType, String diagramType){
        DiagramControllerFactory diagramControllerFactory = new DiagramControllerFactory();
        DiagramController diagramController = diagramControllerFactory.getController(diagramType);
        diagramController.createTree(projectTreeView.getSourceFolderPath());
        diagramController.convertTreeToDiagram(getSelectedFiles(diagramType));
        diagramController.arrangeDiagram();

        if (visualizationType.equals("Export")) {
            FolderChooser folderChooser = new FolderChooser("Export Diagram", menuBar);
            if (folderChooser.getSelectedDirectory() != null) {
                diagramController.exportDiagramToGraphML(folderChooser.getSelectedDirectory().getPath());
            }
        }else {
            try {
                DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
                diagramVisualization.createDiagramVisualization(diagramController.getDiagram());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> getSelectedFiles(String diagramType) {
        if (diagramType.equals("Package")) {
            return projectTreeView.getSelectedFiles(projectTreeView.getFolderFiles());
        }else{
            return projectTreeView.getSelectedFiles(projectTreeView.getJavaSourceFiles());
        }
    }

    public void createTreeView(String sourceFolderPath){
        projectTreeView = new ProjectTreeView(treeView, sourceFolderPath);
        projectTreeView.createTreeView();
    }

}
