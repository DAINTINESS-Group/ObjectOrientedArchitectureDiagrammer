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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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
    
    private SmartGraphPanel<String, String> graphView;
    private Controller diagramController;
    private ProjectTreeView projectTreeView;
    private double graphViewNormalScaleX;
    private double graphViewNormalScaleY;

    public void visualizeGraph(SmartGraphPanel<String, String> graphView) {
    	this.graphView = graphView;
    	ContentZoomPane zoomPane = new ContentZoomPane(graphView);
    	ScrollPane scrollPane = new ScrollPane(zoomPane);
        scrollPane.setPannable(false);
        graphViewNormalScaleX = graphView.getScaleX();
        graphViewNormalScaleY = graphView.getScaleY();
        String graphViewBackgroundColor = "#F4FFFB";
        Color zoomPaneBackgroundColor = Color.web(graphViewBackgroundColor);
        zoomPane.setBackground(new Background(new BackgroundFill(zoomPaneBackgroundColor, null, null)));
        graphView.minWidthProperty().bind(borderPane.widthProperty());
        graphView.minHeightProperty().bind(borderPane.heightProperty());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    	borderPane.setCenter(scrollPane);
        setTreeView(projectTreeView);
        zoomPane.setOnScroll(event -> {
            double zoomFactor = 1.1;
            if (event.getDeltaY() >= 0) {
            	graphView.setScaleX(graphView.getScaleX() * zoomFactor);
            	graphView.setScaleY(graphView.getScaleY() * zoomFactor);
            } else {
            	graphView.setScaleX(graphView.getScaleX() / zoomFactor);
            	graphView.setScaleY(graphView.getScaleY() / zoomFactor);
            }
        });
    }

    public void exportDiagramAsImage() {
        try {
            File selectedDirectory = FileAndDirectoryUtility.saveFile("Export Diagram as PNG", menuBar,"PNG files");
            if (selectedDirectory == null) {
                return;
            }
            double changeScaleX = graphView.getScaleX();
            double changeScaleY = graphView.getScaleY();
            graphView.setScaleX(graphViewNormalScaleX);
            graphView.setScaleY(graphViewNormalScaleY);
            WritableImage image = graphView.snapshot(new SnapshotParameters(), null);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedDirectory);
            graphView.setScaleX(changeScaleX);
            graphView.setScaleY(changeScaleY);
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
    
    public SmartGraphPanel<String, String> applyLayout() {
    	return diagramController.applyLayout();
    }
    
    public void applySugiyama() {
    	SmartGraphPanel<String, String> graphView = diagramController.applySpecificLayout("Sugiyama");
        graphView.update();
    }
    
    public void applyFruchtermanReingold() {
    	SmartGraphPanel<String, String> graphView = diagramController.applySpecificLayout("Fruchterman_Reingold");
        graphView.update();
    }
    
    public void applyAdvancedFruchtermanReingold() {
    	SmartGraphPanel<String, String> graphView = diagramController.applySpecificLayout("Advanced_Fruchterman_Reingold");
        graphView.update();
    }
    
    public void applySpring() {
    	SmartGraphPanel<String, String> graphView = diagramController.applySpecificLayout("Spring");
        graphView.update();
    }
    
    public void applyAdvancedSpring() {
    	SmartGraphPanel<String, String> graphView = diagramController.applySpecificLayout("Advanced_Spring");
        graphView.update();
    }
    
    public void applyKamadaKawai() {
    	SmartGraphPanel<String, String> graphView = diagramController.applySpecificLayout("Kamada_Kawai");
        graphView.update();
    }
    
}
