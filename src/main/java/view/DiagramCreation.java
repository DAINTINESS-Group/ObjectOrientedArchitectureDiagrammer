package view;

import controller.DiagramController;
import controller.DiagramControllerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

import java.io.IOException;
import java.util.List;

public class DiagramCreation {

    @FXML
    MenuBar menuBar;

    private ProjectTreeView projectTreeView;

    public DiagramCreation(MenuBar menuBar, ProjectTreeView projectTreeView) {
        this.menuBar = menuBar;
        this.projectTreeView = projectTreeView;
    }

    public void createDiagram(String visualizationType, String diagramType){
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

}
