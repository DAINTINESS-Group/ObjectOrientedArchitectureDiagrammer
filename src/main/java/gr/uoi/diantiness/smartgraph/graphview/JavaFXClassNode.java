package gr.uoi.diantiness.smartgraph.graphview;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;

public class JavaFXPackageNode {

    private String name;

    public JavaFXPackageNode(String name) {
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

    /**
     * Establishes the shape of the vertex to use when representing this city.
     *
     * @return the name of the shape, see {@link com.brunomnsilva.smartgraph.graphview.ShapeFactory}
     */
    @SmartShapeTypeSource
    public String modelShape() {
        return "package";
    }
}
