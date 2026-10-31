package gr.uoi.smartgraph.graphview.arrow;

import com.brunomnsilva.smartgraph.graphview.SmartArrow;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;

public class SmartNestingArrow extends SmartArrow {

    public SmartNestingArrow(double size) {
        super(size);
        getElements().clear();
        this.getElements().add(new MoveTo(0.0, 0.0));
        this.getElements().add(new ArcTo(size, size, 0.0, -2.0 * size, 0.0, false, true));
        this.getElements().add(new ArcTo(size, size, 0.0, 0.0, 0.0, false, true));
        this.getElements().add(new MoveTo(-2.0 * size, 0.0));
        this.getElements().add(new LineTo(0.0, 0.0));
        this.getElements().add(new MoveTo(-size, -size));
        this.getElements().add(new LineTo(-size, size));
    }
}
