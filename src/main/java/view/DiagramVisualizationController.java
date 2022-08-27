package view;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import controller.Controller;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DiagramVisualizationController {

    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;

    private Controller diagramController;
    private ProjectTreeView projectTreeView;
    private DiagramCreation diagramCreation;

    public void visualizeGraph(SmartGraphPanel<String, String> graphView, String diagramType) {
        borderPane.setCenter(new ContentZoomPane(graphView));
        diagramCreation = new DiagramCreation(projectTreeView, menuBar);
        if (diagramType.equals("new")) {
            setTreeView(projectTreeView);
        }
    }

    public void createProject(ActionEvent event) {
        diagramCreation.createProject(((MenuItem) event.getSource()).getText());
    }

    public void loadProject() {
        diagramCreation.loadProject();
    }

    public void viewProject(ActionEvent event) {
        diagramCreation.viewProject(((MenuItem) event.getSource()).getText());
    }

    public void openProject() {
        MenuUtility.openProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void quitApp() {
        MenuUtility.quitApp(menuBar);
    }

    public void ExportDiagram() {
        File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram as PNG", menuBar,"PNG files");
        if (selectedDirectory != null) {
            WritableImage image = borderPane.getCenter().snapshot(new SnapshotParameters(), null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedDirectory);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDiagram() {
        File selectedFile = FileAndDirectoryUtility.saveFile("Save Diagram", menuBar, "Text Files");
        if (selectedFile != null) {
            diagramController.saveDiagram(selectedFile.toPath());
        }
    }

    public void loadDiagram() {
        PopupWindow.createPopupInfoWindow("Close the current diagram first!", "Error");
    }

    public void aboutPage() { MenuUtility.aboutPage(menuBar); }

    public void setDiagramController(Controller diagramController) {
        this.diagramController = diagramController;
    }

    public void setTreeView(ProjectTreeView projectTreeView) {
        this.projectTreeView = projectTreeView;
        borderPane.setLeft(projectTreeView.treeView);
    }
}
