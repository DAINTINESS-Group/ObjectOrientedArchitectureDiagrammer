package view;

import controller.Controller;
import controller.DiagramControllerFactory;
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
        this.diagramType = diagramType;
        if (projectTreeView == null) {
            PopupWindow.createPopupInfoWindow("You should open a new project first!", "Error");
            return;
        }
        DiagramControllerFactory diagramControllerFactory = new DiagramControllerFactory();
        diagramController = diagramControllerFactory.getDiagramController(diagramType);
        diagramController.createTree(projectTreeView.getSourceFolderPath());
    }

    public void loadProject() {
        projectTreeView.setCheckedItems(projectTreeView.getRootItem());
        diagramController.convertTreeToDiagram(getSelectedFiles(diagramType));
    }

    private List<String> getSelectedFiles(String diagramType) {
        if (diagramType.equals("Package")) {
            return projectTreeView.getSelectedFiles(projectTreeView.getFolderFiles(), "package");
        }else{
            return projectTreeView.getSelectedFiles(projectTreeView.getJavaSourceFiles(), "java");
        }
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

    private boolean wereFilesChosen() {
        return !(projectTreeView.getSelectedFiles(projectTreeView.getFolderFiles(), "package").size() == 0 &&
                projectTreeView.getSelectedFiles(projectTreeView.getJavaSourceFiles(), "java").size() == 0);
    }

    private void viewDiagram(){
        DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
        diagramVisualization.setDiagramController(diagramController);
        diagramVisualization.setProjectTreeView(projectTreeView);
        diagramVisualization.loadDiagramVisualization(diagramController.visualizeJavaFXGraph(), "new");
    }

    public void exportDiagram(){
        File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram", menuBar, "GraphML Files");
        if (selectedDirectory != null) {
            diagramController.arrangeDiagram();
            diagramController.exportDiagramToGraphML(selectedDirectory.toPath());
        }
    }
    
    public void exportPlantUMLDiagram() {
    	File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram As PlantUML", menuBar, "PlantUML Files");
        if (selectedDirectory != null) {
            diagramController.exportPlantUMLDiagram(selectedDirectory.toPath());
        }
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
