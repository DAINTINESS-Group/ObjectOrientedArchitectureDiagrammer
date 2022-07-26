package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.net.URL;

public class MenuUtility {

    private MenuUtility(){
        throw new java.lang.UnsupportedOperationException("Not to be instantiated");
    }

    public static void loadProject(MenuBar menuBar){
        FolderChooser folderChooser = new FolderChooser("Load Project Source Folder", menuBar);
        if (folderChooser.getSelectedDirectory() != null) {
            try {
                URL url = MenuUtility.class.getResource("/fxml/DiagramCreationView.fxml");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(url);
                Parent diagramCreationParent = loader.load();

                DiagramCreationController controller = loader.getController();
                controller.createTreeView(folderChooser.getSelectedDirectory().getPath());

                Scene diagramCreationScene = new Scene(diagramCreationParent);
                Stage window = (Stage) menuBar.getScene().getWindow();
                window.setScene(diagramCreationScene);
                window.show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
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
