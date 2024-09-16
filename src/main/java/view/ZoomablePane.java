package view;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;

public class ZoomablePane extends ScrollPane
{
    private static final double zoomIntensity = 0.02;

    private final Node   target;
    private final Node   zoomNode;
    private       double scaleValue = 0.8;


    public ZoomablePane(Node target)
    {
        super();
        this.target = target;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));
        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setFitToHeight(true); //Center.
        setFitToWidth(true);  //Center.
        updateScale();
    }


    private Node outerNode(Node node)
    {
        Node outerNode = centeredNode(node);
        outerNode.setOnScroll(this::onScroll);
        return outerNode;
    }


    private Node centeredNode(Node node)
    {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }


    private void updateScale()
    {
        target.setScaleX(scaleValue);
        target.setScaleY(scaleValue);
    }


    private void onScroll(ScrollEvent event)
    {
        double  wheelDelta = event.getTextDeltaY();
        Point2D mousePoint = new Point2D(event.getX(), event.getY());
        double  zoomFactor = Math.exp(wheelDelta * zoomIntensity);

        Bounds innerBounds    = zoomNode.getLayoutBounds();
        Bounds viewportBounds = getViewportBounds();

        // Calculate pixel offsets from [0, 1] range.
        double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
        double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

        scaleValue = scaleValue * zoomFactor;
        updateScale();
        this.layout(); // Refresh ScrollPane scroll positions & target bounds.

        // Convert target coordinates to zoomTarget coordinates.
        Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

        // Calculate adjustment of scroll position (pixels).
        Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

        // Convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane).
        Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
        this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
        this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
    }
}
