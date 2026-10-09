package gr.uoi.ooad.model.diagram.javafx;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;

public interface JavaFXUMLNode {

    @SmartLabelSource
    public abstract String getName();

    @SmartShapeTypeSource
    public abstract String modelShape();
}
