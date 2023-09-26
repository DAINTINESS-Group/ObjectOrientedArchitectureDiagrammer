package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AboutPageController implements Initializable {

	@FXML
	MenuBar menuBar;
	@FXML
	BorderPane borderPane;

	public void newProject() {
		MenuUtility.openProject(menuBar);
	}

	public void quitApp() {
		MenuUtility.quitApp(menuBar);
	}

	public void aboutPage() { MenuUtility.aboutPage(menuBar); }

	public void initialize(URL url, ResourceBundle resourceBundle) {
		WebView webView = new WebView();
		webView.setZoom(1.2);
		WebEngine webEngine = webView.getEngine();
		URL documentationUrl = Objects.requireNonNull(AboutPageController.class.getResource("/assets/UserDocumentation.html"));
		webEngine.load(documentationUrl.toString());
		borderPane.setCenter(webView);
	}
}
