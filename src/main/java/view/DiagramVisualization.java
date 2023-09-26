package view;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class DiagramVisualization {

	private static final int EDGE_STARTING_NODE = 0;
	private static final int EDGE_ENDING_NODE = 1;
	private static final int EDGE_TYPE = 2;

	@FXML
	MenuBar menuBar;

	private ProjectTreeView projectTreeView;
	private Controller diagramController;
	private SmartGraphPanel<String, String> graphView;

	public DiagramVisualization(MenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public void loadDiagramVisualization(SmartGraphPanel<String, String> graphView) {
		this.graphView = graphView;
		try {
			URL url = getClass().getResource("/fxml/DiagramVisualizationView.fxml");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(url);
			Parent diagramVisualizationParent = loader.load();
			DiagramVisualizationController diagramVisualizationController = loader.getController();
			diagramVisualizationController.setDiagramController(diagramController);
			diagramVisualizationController.setTreeView(projectTreeView);
			addGraphActions();
			diagramVisualizationController.visualizeGraph(graphView);
			Scene diagramVisualizationScene = new Scene(diagramVisualizationParent);
			Stage window = (Stage) menuBar.getScene().getWindow();
			window.setScene(diagramVisualizationScene);
			window.show();
			graphView.init();
			graphView = diagramVisualizationController.applyLayout();
			graphView.update();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadLoadedDiagramVisualization(SmartGraphPanel<String, String> graphView) {
		this.graphView = graphView;
		try {
			URL url = getClass().getResource("/fxml/ProjectLoadView.fxml");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(url);
			Parent parent = loader.load();
			ProjectLoadController projectLoadController = loader.getController();
			projectLoadController.visualizeGraph(graphView);
			projectLoadController.setDiagramController(diagramController);
			Scene diagramVisualizationScene = new Scene(parent);
			Stage window = (Stage) menuBar.getScene().getWindow();
			window.setScene(diagramVisualizationScene);
			window.show();
			try {
				graphView = diagramController.visualizeLoadedJavaFXGraph();
				graphView.init();
				graphView.update();
			} catch (IllegalStateException e) {
				// Just continue. Handling here the expection in order not showing the error to the user.
				// We do this, because this error doesn't affect the system.
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addGraphActions() {
		addVertexActions();
		addEdgeActions();
	}

	private void addVertexActions() {
		graphView.setVertexDoubleClickAction(graphVertex ->
		PopupWindow.createPopupInfoWindow(String.format("Vertex contains element: %s", graphVertex.getUnderlyingVertex().element()),
				"Node Information"));
	}

	private void addEdgeActions() {
		graphView.setEdgeDoubleClickAction(graphEdge ->
		PopupWindow.createPopupInfoWindow(String.format("Edge starting node: %s", graphEdge.getUnderlyingEdge().element().split("_")[EDGE_STARTING_NODE]) +
				"\n" + String.format("Edge ending node: %s", graphEdge.getUnderlyingEdge().element().split("_")[EDGE_ENDING_NODE]) +
				"\n" + String.format("Type of relationship: %s", Character.toUpperCase(graphEdge.getUnderlyingEdge().element().split("_")[EDGE_TYPE].charAt(0)) +
						graphEdge.getUnderlyingEdge().element().split("_")[EDGE_TYPE].substring(1)), "Edge Information"));
	}

	public void setDiagramController(Controller diagramController) {
		this.diagramController = diagramController;
	}

	public void setProjectTreeView(ProjectTreeView projectTreeView) { this.projectTreeView = projectTreeView; }
}
