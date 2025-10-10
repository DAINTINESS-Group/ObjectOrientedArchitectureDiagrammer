package gr.uoi.ooad.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupWindow {

    private PopupWindow() {
        throw new java.lang.UnsupportedOperationException("Not to be instantiated");
    }

    public static void createPopupInfoWindow(String infoMessage, String popupWindowTitle) {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle(popupWindowTitle);
        Label label1 = new Label(infoMessage);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popupWindow.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, closeButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 150);
        scene1.getStylesheets().add("styles/modena_dark.css");
        popupWindow.setScene(scene1);
        popupWindow.showAndWait();
    }
}
