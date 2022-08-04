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

    private final ProjectTreeView projectTreeView;

    public DiagramCreation(MenuBar menuBar, ProjectTreeView projectTreeView) {
        this.menuBar = menuBar;
        this.projectTreeView = projectTreeView;
    }

    public void createDiagram(String visualizationType, String diagramType){
        DiagramControllerFactory diagramControllerFactory = new DiagramControllerFactory();
        Controller diagramController = diagramControllerFactory.getDiagramController(diagramType);
        diagramController.createTree(projectTreeView.getSourceFolderPath());
        diagramController.convertTreeToDiagram(getSelectedFiles(diagramType));
        diagramController.arrangeDiagram();

        if (visualizationType.equals("Export")) {
            File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram", menuBar, "GraphML Files");
            if (selectedDirectory != null) {
                diagramController.exportDiagramToGraphML(selectedDirectory.getPath());
            }
        }else {
            DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
            diagramVisualization.setDiagramController(diagramController);
            diagramVisualization.loadDiagramVisualization(diagramController.visualizeJavaFXGraph());
        }
    }

    private List<String> getSelectedFiles(String diagramType) {
        if (diagramType.equals("Package")) {
            return projectTreeView.getSelectedFiles(projectTreeView.getFolderFiles());
        }else{
            return projectTreeView.getSelectedFiles(projectTreeView.getJavaSourceFiles());
        }
    }

}
