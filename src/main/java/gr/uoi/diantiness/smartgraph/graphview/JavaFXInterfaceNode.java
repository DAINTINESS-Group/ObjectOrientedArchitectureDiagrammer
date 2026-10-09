package gr.uoi.diantiness.smartgraph.graphview;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;

import java.util.Objects;

public class JavaFXInterfaceNode extends JavaFXUMLNode {

    public JavaFXInterfaceNode(String name) {
        super(name);
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
        JavaFXInterfaceNode that = (JavaFXInterfaceNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
