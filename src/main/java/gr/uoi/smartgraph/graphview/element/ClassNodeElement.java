package gr.uoi.smartgraph.graphview.element;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;
import java.util.Objects;

public class ClassNodeElement implements UMLNodeElement {

    protected String name;

    public ClassNodeElement(String name) {
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
        return "class";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClassNodeElement that = (ClassNodeElement) o;
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
