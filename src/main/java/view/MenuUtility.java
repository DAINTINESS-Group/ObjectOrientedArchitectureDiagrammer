package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class MenuUtility {

    private MenuUtility(){
        throw new java.lang.UnsupportedOperationException("Not to be instantiated");
    }

    public static void newProject(MenuBar menuBar){
        File selectedDirectory = FileAndDirectoryUtility.chooseDirectory("Load Project Source Folder", menuBar);
        if (selectedDirectory != null) {
            try {
                URL url = MenuUtility.class.getResource("/fxml/DiagramCreationView.fxml");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(url);
                Parent diagramCreationParent = loader.load();

                DiagramCreationController controller = loader.getController();
                controller.createTreeView(selectedDirectory.getPath());

                Scene diagramCreationScene = new Scene(diagramCreationParent);
                Stage window = (Stage) menuBar.getScene().getWindow();
                window.setScene(diagramCreationScene);
                window.show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static boolean isCalledByTheVisualizationController() {
        return Thread.currentThread().getStackTrace()[2].getClassName().equals("view.DiagramVisualizationController");
    }

    public static void closeProject(MenuBar menuBar){
        try {
            URL url = MenuUtility.class.getResource("/fxml/ProjectLoadView.fxml");
            Parent diagramCreationParent = FXMLLoader.load(url);
            Scene diagramCreationScene = new Scene(diagramCreationParent);
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(diagramCreationScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void quitApp(MenuBar menuBar){
        Stage window = (Stage) menuBar.getScene().getWindow();
        window.close();
    }
}
