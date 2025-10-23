package gr.uoi.smartgraph.graphview.element;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;

/** Class for element objects that adorn each SmartGraph node */
public interface UMLNodeElement {

    @SmartLabelSource
    public abstract String getName();

    @SmartShapeTypeSource
    public abstract String modelShape();
}
