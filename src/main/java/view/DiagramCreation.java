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

    private Controller diagramController;
    private final ProjectTreeView projectTreeView;
    private String diagramType;

    public DiagramCreation(ProjectTreeView projectTreeView, MenuBar menuBar) {
        this.diagramType = "";
        this.menuBar = menuBar;
        this.projectTreeView = projectTreeView;
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
        if (diagramType.isEmpty()) {
            PopupWindow.createPopupInfoWindow("You haven't created a diagram yet!", "Error");
            return;
        }
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

    public void viewProject(String visualizationType) {
        if (diagramType.isEmpty()) {
            PopupWindow.createPopupInfoWindow("You have neither created a diagram nor loaded it yet!", "Error");
            return;
        }
        if (!wereFilesChosen()) {
            PopupWindow.createPopupInfoWindow("You haven't selected any files!", "Error");
        }else {
            createDiagram(visualizationType);
        }
    }

    private boolean wereFilesChosen() {
        return !(projectTreeView.getSelectedFiles(projectTreeView.getFolderFiles(), "package").size() == 0 &&
                projectTreeView.getSelectedFiles(projectTreeView.getJavaSourceFiles(), "java").size() == 0);
    }

    private void createDiagram(String visualizationType){
        if (visualizationType.equals("Export")) {
            File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram", menuBar, "GraphML Files");
            if (selectedDirectory != null) {
                diagramController.arrangeDiagram();
                diagramController.exportDiagramToGraphML(selectedDirectory.toPath());
            }
        }else {
            DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
            diagramVisualization.setDiagramController(diagramController);
            diagramVisualization.setProjectTreeView(projectTreeView);
            diagramVisualization.loadDiagramVisualization(diagramController.visualizeJavaFXGraph(), "new");
        }
    }
}
