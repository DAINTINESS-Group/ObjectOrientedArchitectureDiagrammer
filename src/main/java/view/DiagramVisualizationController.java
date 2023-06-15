package view;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import controller.Controller;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.MenuBar;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DiagramVisualizationController {

    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;

    private Controller diagramController;
    private ProjectTreeView projectTreeView;

    public void visualizeGraph(SmartGraphPanel<String, String> graphView) {
        borderPane.setCenter(new ContentZoomPane(graphView));
        setTreeView(projectTreeView);
    }

    public void exportDiagramAsImage() {
        try {
            File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram as PNG", menuBar,"PNG files");
            if (selectedDirectory == null) {
                return;
            }
            WritableImage image = borderPane.getCenter().snapshot(new SnapshotParameters(), null);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedDirectory);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportDiagramAsText() {
        File selectedFile = FileAndDirectoryUtility.saveFile("Save Diagram", menuBar, "Text Files");
        if (selectedFile == null) {
            return;
        }
        diagramController.saveDiagram(selectedFile.toPath());
    }

    public void closeDiagram() {
        try {
            URL url = MenuUtility.class.getResource("/fxml/DiagramCreationView.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);
            Parent diagramCreationParent = loader.load();

            DiagramCreationController diagramCreationController = loader.getController();
            diagramCreationController.setProject();

            Scene diagramCreationScene = new Scene(diagramCreationParent);
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(diagramCreationScene);
            window.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void exportDiagramAsGraphML() {
        DiagramCreation.getInstance().exportDiagram();
    }
    
    public void exportDiagramAsPlantUML() {
    	DiagramCreation.getInstance().exportPlantUMLImage();
    }
    
    public void exportDiagramAsPlantUMLText() {
    	DiagramCreation.getInstance().exportPlantUMLText();
    }

    public void aboutPage() { MenuUtility.aboutPage(menuBar); }

    public void showInfoWindow() {
        PopupWindow.createPopupInfoWindow("Close the current diagram first!", "Error");
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

    public void setDiagramController(Controller diagramController) {
        this.diagramController = diagramController;
    }

    public void setTreeView(ProjectTreeView projectTreeView) {
        this.projectTreeView = projectTreeView;
        borderPane.setLeft(projectTreeView.treeView);
    }
}
