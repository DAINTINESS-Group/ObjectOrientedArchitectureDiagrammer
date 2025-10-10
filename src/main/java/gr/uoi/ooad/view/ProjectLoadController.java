package gr.uoi.ooad.view;

import com.brunomnsilva.smartgraph.containers.ContentZoomPane;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import gr.uoi.ooad.controller.Controller;
import gr.uoi.ooad.model.diagram.javafx.JavaFXUMLNode;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;

public class ProjectLoadController {

    @FXML MenuBar menuBar;
    @FXML BorderPane borderPane;
    @FXML Menu exportMenu;

    private SmartGraphPanel<JavaFXUMLNode, String> graphView;
    private double graphViewNormalScaleX;
    private double graphViewNormalScaleY;
    private Controller diagramController;

    public void openProject() {
        MenuUtility.openProject(menuBar);
    }

    public void closeProject() {
        MenuUtility.closeProject(menuBar);
    }

    public void quitApp() {
        MenuUtility.quitApp(menuBar);
    }

    public void aboutPage() {
        MenuUtility.aboutPage(menuBar);
    }

    public void loadDiagram(ActionEvent event) {
        FileUtility.setLoadedDiagramName(MenuUtility.loadDiagram(menuBar, event));
    }

    public void visualizeGraph(SmartGraphPanel<JavaFXUMLNode, String> graphView) {
        this.graphView = graphView;
        ContentZoomPane zoomPane = new ContentZoomPane(graphView);
        ScrollPane scrollPane = new ScrollPane(zoomPane);
        scrollPane.setPannable(false);
        graphViewNormalScaleX = graphView.getScaleX();
        graphViewNormalScaleY = graphView.getScaleY();
        String graphViewBackgroundColor = "#F4FFFB";
        Color zoomPaneBackgroundColor = Color.web(graphViewBackgroundColor);
        zoomPane.setBackground(
                new Background(new BackgroundFill(zoomPaneBackgroundColor, null, null)));
        graphView.minWidthProperty().bind(borderPane.widthProperty());
        graphView.minHeightProperty().bind(borderPane.heightProperty());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        borderPane.setCenter(scrollPane);
        zoomPane.setOnScroll(
                event -> {
                    double zoomFactor = 1.1;
                    if (event.getDeltaY() >= 0) {
                        graphView.setScaleX(graphView.getScaleX() * zoomFactor);
                        graphView.setScaleY(graphView.getScaleY() * zoomFactor);
                    } else {
                        graphView.setScaleX(graphView.getScaleX() / zoomFactor);
                        graphView.setScaleY(graphView.getScaleY() / zoomFactor);
                    }
                });
        exportMenu.setVisible(true);
    }

    public void exportDiagramAsImage() {
        try {
            File selectedDirectory =
                    FileUtility.saveLoadedFile("Export Diagram as PNG", menuBar, "PNG files");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportDiagramAsText() {
        File selectedFile = FileUtility.saveLoadedFile("Save Diagram", menuBar, "Text Files");
        if (selectedFile == null) {
            return;
        }
        diagramController.saveDiagram(selectedFile.toPath());
    }

    public void closeDiagram() {
        MenuUtility.closeProject(menuBar);
        exportMenu.setVisible(false);
    }

    public void setDiagramController(Controller diagramController) {
        this.diagramController = diagramController;
    }
}
