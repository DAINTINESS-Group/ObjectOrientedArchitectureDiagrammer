package gr.uoi.smartgraph.graphview.element;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;
import java.util.Objects;

public class InterfaceNodeElement implements UMLNodeElement {

    protected String name;

    public InterfaceNodeElement(String name) {
        this.name = name;
    }

    /**
     * Establishes the shape of the vertex to use when representing this city.
     *
     * @return the name of the shape, see {@link com.brunomnsilva.smartgraph.graphview.ShapeFactory}
     */
    @Override
    @SmartShapeTypeSource
    public String modelShape() {
        return "interface";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InterfaceNodeElement that = (InterfaceNodeElement) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @SmartLabelSource
    @Override
    public String getName() {
        return "<<interface>>\n" + name;
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
