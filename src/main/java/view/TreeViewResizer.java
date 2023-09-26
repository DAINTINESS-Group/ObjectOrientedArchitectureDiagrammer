package view;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class TreeViewResizer {

	private static final int MIN_TREEVIEW_WIDTH = 100;
	private static final int MAX_TREEVIEW_WIDTH = 800;
	private Region region;
	private double mouseX;

	public TreeViewResizer() {
	}

	public void makeResizable(Region region) {
		this.region = region;
		region.setOnMousePressed(onMousePressedEventHandler);
		region.setOnMouseDragged(onMouseDraggedEventHandler);
		region.setOnMouseMoved(onMouseMovedEventHandler);
		region.setOnMouseReleased(onMouseReleasedEventHandler);
	}

	EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
		mouseX = event.getSceneX();
	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
		double deltaX = event.getSceneX() - mouseX;

		if (!Cursor.DEFAULT.equals(region.getCursor())) {
			if (region.getCursor().equals(Cursor.E_RESIZE)) {
				double newWidth = region.getWidth() + deltaX;
				newWidth = Math.max(MIN_TREEVIEW_WIDTH, Math.min(newWidth, MAX_TREEVIEW_WIDTH));
				region.setPrefWidth(newWidth);
				//region.setPrefWidth(region.getWidth() + deltaX);
			} else if (region.getCursor().equals(Cursor.W_RESIZE)) {
				double newWidth = region.getWidth() - deltaX;
				newWidth = Math.max(MIN_TREEVIEW_WIDTH, Math.min(newWidth, MAX_TREEVIEW_WIDTH));
				region.setPrefWidth(newWidth);
				//region.setPrefWidth(region.getWidth() - deltaX);
				region.setTranslateX(region.getTranslateX() + deltaX);
			}
		}

		mouseX = event.getSceneX();
	};

	EventHandler<MouseEvent> onMouseMovedEventHandler = event -> {
		double sceneX = event.getSceneX();
		double nodeX = region.getBoundsInParent().getMinX();
		double nodeWidth = region.getBoundsInParent().getWidth();

		// By applying different number in the if else statements below,
		// we control the 'thickness' of the line that is selected to resize the treeView

		if (sceneX > nodeX + nodeWidth - 10 && sceneX < nodeX + nodeWidth + 10) {
			region.setCursor(Cursor.E_RESIZE);
		} else if (sceneX > nodeX - 10 && sceneX < nodeX + 10) {
			region.setCursor(Cursor.W_RESIZE);
		} else {
			region.setCursor(Cursor.DEFAULT);
		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
		region.setCursor(Cursor.DEFAULT);
	};
}