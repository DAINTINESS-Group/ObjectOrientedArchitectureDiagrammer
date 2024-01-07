package view;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AboutPageController implements Initializable
{

    public static final String DOCUMENTATION = "https://drive.google.com/file/d/17h9-hPtQ7GXwKxacQCjEKP51aE3G2JdZ/view";


    @FXML
    MenuBar    menuBar;
    @FXML
    BorderPane borderPane;


    public void newProject()
    {
        MenuUtility.openProject(menuBar);
    }


    public void quitApp()
    {
        MenuUtility.quitApp(menuBar);
    }


    public void aboutPage() {MenuUtility.aboutPage(menuBar);}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        WebView webView = new WebView();
        webView.setZoom(1.2);
        WebEngine webEngine        = webView.getEngine();
        webEngine.load(DOCUMENTATION);
        borderPane.setCenter(webView);
    }
}
