package gr.uoi.smartgraph.graphview.element;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;

public interface JavaFXUMLNode {

    @SmartLabelSource
    public abstract String getName();

    @SmartShapeTypeSource
    public abstract String modelShape();
}
