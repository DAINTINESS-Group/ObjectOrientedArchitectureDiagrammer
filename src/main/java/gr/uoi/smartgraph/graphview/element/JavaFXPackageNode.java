package gr.uoi.smartgraph.graphview.element;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;
import java.util.Objects;

public class JavaFXPackageNode implements JavaFXUMLNode {

    protected String name;

    public JavaFXPackageNode(String name) {
        this.name = name;
    }

    /**
     * Establishes the shape of the vertex to use when representing this city.
     *
     * @return the name of the shape, see {@link com.brunomnsilva.smartgraph.graphview.ShapeFactory}
     */
    @SmartShapeTypeSource
    @Override
    public String modelShape() {
        return "package";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JavaFXPackageNode that = (JavaFXPackageNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @SmartLabelSource
    @Override
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
}
