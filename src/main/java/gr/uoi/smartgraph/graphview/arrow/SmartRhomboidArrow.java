package gr.uoi.smartgraph.graphview.arrow;

import com.brunomnsilva.smartgraph.graphview.SmartArrow;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;

public class SmartRhomboidArrow extends SmartArrow {
    public SmartRhomboidArrow(double size) {
        super(size);
        getElements().clear();
        this.getElements().add(new MoveTo((double) 0.0F, (double) 0.0F));
        this.getElements().add(new LineTo((double) -2.0F * size, size));
        this.getElements().add(new LineTo((double) -4.0F * size, (double) 0.0F));
        this.getElements().add(new LineTo((double) -2.0F * size, -size));
        this.getElements().add(new LineTo((double) 0.0F, (double) 0.0F));
    }
}
