package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupWindow {
/*
    public void createPopupWindow(String buttonsText) {
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
    
 */

    public void createPopupErrorWindow() {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Error");
        Label label1= new Label("No files were chosen");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            popupWindow.close();
        });
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 400, 250);
        scene1.getStylesheets().add("styles/modena_dark.css");
        popupWindow.setScene(scene1);
        popupWindow.showAndWait();
    }
}
