package gr.uoi.diantiness.smartgraph.graphview;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;

public abstract class JavaFXUMLNode {
    protected String name;

    public JavaFXUMLNode(String name) {
        this.name = name;
    }

    @SmartLabelSource
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the city.
     *
     * @param name the name of the city
     */
    public void setName(String name) {
        this.name = name;
    }

    @SmartShapeTypeSource
    public abstract String modelShape();
}
