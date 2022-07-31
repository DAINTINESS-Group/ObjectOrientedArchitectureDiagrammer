package view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class PopupWindow {

    @FXML
    MenuBar menuBar;
    private ProjectTreeView projectTreeView;

    public PopupWindow(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public void createDiagramPopupWindow(String buttonsText, ProjectTreeView projectTreeView) {
        this.projectTreeView = projectTreeView;
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Choose how to view diagram");
        Label label1= new Label("Export as GraphML or visualize here");

        Button exportButton= new Button("Export as GraphML");
        exportButton.setOnAction(e -> {
            chooseFiles(buttonsText, "Export");
            popupWindow.close();
        });

        Button visualizeButton = new Button("Visualize here");
        visualizeButton.setOnAction(e -> {
            chooseFiles(buttonsText, "View");
            popupWindow.close();
        });

        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, exportButton, visualizeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 400, 250);
        scene1.getStylesheets().add("styles/modena_dark.css");
        popupWindow.setScene(scene1);
        popupWindow.showAndWait();
    }

    private void chooseFiles(String fileType, String visualizationType) {
        DiagramCreation diagramCreation = new DiagramCreation(menuBar, projectTreeView);
        diagramCreation.createDiagram(visualizationType, fileType.split(" ")[0]);
    }

    public void createPopupInfoWindow(String infoMessage, String popupWindowTitle) {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle(popupWindowTitle);
        Label label1= new Label(infoMessage);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popupWindow.close());
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 400, 250);
        scene1.getStylesheets().add("styles/modena_dark.css");
        popupWindow.setScene(scene1);
        popupWindow.showAndWait();
    }

}
