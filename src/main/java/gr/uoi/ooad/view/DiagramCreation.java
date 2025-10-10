package gr.uoi.ooad.view;

import static gr.uoi.ooad.view.FileType.PACKAGE;
import static gr.uoi.ooad.view.FileType.SOURCE;

import gr.uoi.ooad.controller.Controller;
import gr.uoi.ooad.controller.ControllerFactory;
import java.io.File;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

public class DiagramCreation {

    public static final String UML = "uml";

    @FXML MenuBar menuBar;

    private static DiagramCreation instance;
    private Controller diagramController;
    private ProjectTreeView projectTreeView;
    private String diagramType;

    private DiagramCreation() {
        diagramType = "";
    }

    public void createProject(String diagramType) {
        if (projectTreeView == null) {
            PopupWindow.createPopupInfoWindow("You should load a project first!", "Error");
            return;
        }

        this.diagramType = diagramType;
        diagramController = ControllerFactory.createController(UML, diagramType);
        diagramController.createTree(projectTreeView.getSourceFolderPath());
    }

    public void loadProject() {
        projectTreeView.setCheckedItems(projectTreeView.getRootItem());
        diagramController.convertTreeToDiagram(getSelectedFiles(diagramType));
        diagramController.arrangeDiagram();
    }

    public void viewProject(String type) {
        if (diagramType.isEmpty()) {
            PopupWindow.createPopupInfoWindow(
                    "You have neither created a diagram nor loaded it yet!", "Error");
            return;
        }
        if (!wereAnyFilesChosen()) {
            PopupWindow.createPopupInfoWindow("You haven't selected any files!", "Error");
            return;
        }

        switch (type) {
            case "smartgraph":
                viewDiagram();
                break;
            case "plantuml":
                viewSvgDiagram();
                break;
            default:
                // Should not happen.
                throw new IllegalArgumentException("Unsupported");
        }
    }

    private void viewDiagram() {
        DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
        diagramVisualization.setDiagramController(diagramController);
        diagramVisualization.setProjectTreeView(projectTreeView);
        diagramVisualization.loadDiagramVisualization(diagramController.visualizeJavaFXGraph());
    }

    private void viewSvgDiagram() {
        DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
        diagramVisualization.setDiagramController(diagramController);
        diagramVisualization.setProjectTreeView(projectTreeView);
        diagramVisualization.loadSvgDiagram();
    }

    public void exportDiagram() {
        File selectedDirectory = FileUtility.saveFile("Export Diagram", menuBar, "GraphML Files");
        if (selectedDirectory == null) return;

        diagramController.exportDiagramToGraphML(selectedDirectory.toPath());
    }

    public void exportPlantUMLImage() {
        File selectedDirectory =
                FileUtility.saveFile("Export Diagram As PlantUML", menuBar, "PlantUML Files");
        if (selectedDirectory == null) return;

        diagramController.exportPlantUMLDiagram(selectedDirectory.toPath());
    }

    public void exportPlantUMLText() {
        File selectedDirectory =
                FileUtility.saveFile("Export PlantUML Text", menuBar, "PlantUML Text Files");
        if (selectedDirectory == null) return;

        diagramController.exportPlantUMLText(selectedDirectory.toPath());
    }

    private List<String> getSelectedFiles(String diagramType) {
        return diagramType.equals("Package")
                ? projectTreeView.getSelectedFiles(PACKAGE)
                : projectTreeView.getSelectedFiles(SOURCE);
    }

    private boolean wereAnyFilesChosen() {
        return !(projectTreeView.getSelectedFiles(PACKAGE).isEmpty()
                && projectTreeView.getSelectedFiles(SOURCE).isEmpty());
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

    public static DiagramCreation getInstance() {
        if (instance == null) {
            instance = new DiagramCreation();
        }
        return instance;
    }
}
