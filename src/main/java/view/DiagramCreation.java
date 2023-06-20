package view;

import controller.Controller;
import controller.DiagramController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

import java.io.File;
import java.util.List;

public class DiagramCreation {

    @FXML
    MenuBar menuBar;

    private static DiagramCreation instance;
    private Controller diagramController;
    private ProjectTreeView projectTreeView;
    private String diagramType;

    private DiagramCreation() {
        this.diagramType = "";
    }

    public void createProject(String diagramType) {
        if (projectTreeView == null) {
            PopupWindow.createPopupInfoWindow("You should load a project first!", "Error");
            return;
        }
        this.diagramType = diagramType;
        diagramController = new DiagramController(diagramType);
        diagramController.createTree(projectTreeView.getSourceFolderPath());
    }

    public void loadProject() {
        projectTreeView.setCheckedItems(projectTreeView.getRootItem());
        diagramController.convertTreeToDiagram(getSelectedFiles(diagramType));
    }

    public void viewProject() {
        if (diagramType.isEmpty()) {
            PopupWindow.createPopupInfoWindow("You have neither created a diagram nor loaded it yet!", "Error");
            return;
        }
        if (!wereFilesChosen()) {
            PopupWindow.createPopupInfoWindow("You haven't selected any files!", "Error");
            return;
        }
        viewDiagram();
    }

    private void viewDiagram(){
        DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
        diagramVisualization.setDiagramController(diagramController);
        diagramVisualization.setProjectTreeView(projectTreeView);
        diagramVisualization.loadDiagramVisualization(diagramController.visualizeJavaFXGraph(), diagramController.getEdgeCollection(), diagramController.getVertexCollection());
    }

    public void exportDiagram(){
        File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram", menuBar, "GraphML Files");
        if (selectedDirectory == null) {
            return;
        }
        diagramController.arrangeDiagram();
        diagramController.exportDiagramToGraphML(selectedDirectory.toPath());
    }

    public void exportPlantUMLImage() {
    	File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram As PlantUML", menuBar, "PlantUML Files");
        if (selectedDirectory == null) {
            return;
        }
        diagramController.exportPlantUMLDiagram(selectedDirectory.toPath());
    }

    public void exportPlantUMLText() {
    	File selectedDirectory = FileAndDirectoryUtility.saveFile("Export PlantUML Text", menuBar, "PlantUML Text Files");
        if (selectedDirectory == null) {
            return;
        }
        diagramController.exportPlantUMLText(selectedDirectory.toPath());
    }

    private List<String> getSelectedFiles(String diagramType) {
        if (diagramType.equals("Package")) {
            return projectTreeView.getSelectedFiles(projectTreeView.getFolderFiles(), "package");
        }else{
            return projectTreeView.getSelectedFiles(projectTreeView.getJavaSourceFiles(), "java");
        }
    }

    private boolean wereFilesChosen() {
        return !(projectTreeView.getSelectedFiles(projectTreeView.getFolderFiles(), "package").size() == 0 &&
                projectTreeView.getSelectedFiles(projectTreeView.getJavaSourceFiles(), "java").size() == 0);
    }

    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public ProjectTreeView getProjectTreeView() {
        return projectTreeView;
    }

    public void setProjectTreeView(ProjectTreeView projectTreeView) {
        this.projectTreeView = projectTreeView;
    }

    public static DiagramCreation getInstance(){
        if (instance == null) {
            instance = new DiagramCreation();
        }
        return instance;
    }
}
