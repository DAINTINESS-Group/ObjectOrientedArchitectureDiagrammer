package view;

import controller.Controller;
import controller.DiagramControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        Controller diagramController = diagramControllerFactory.getController(diagramType);
        diagramController.createTree(projectTreeView.getSourceFolderPath());
        Map<String, Map<String, String>> diagram = diagramController.convertTreeToDiagram(getSelectedFiles(diagramType));
        diagramController.arrangeDiagram();

        if (visualizationType.equals("Export")) {
            FolderChooser folderChooser = new FolderChooser("Export Diagram", menuBar);
            if (folderChooser.getSelectedDirectory() != null) {
                diagramController.exportDiagramToGraphML(folderChooser.getSelectedDirectory().getPath());
            }
        }else {
            try {
                DiagramVisualization diagramVisualization = new DiagramVisualization(menuBar);
                diagramVisualization.createDiagramVisualization(diagram, diagramType);
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

}
