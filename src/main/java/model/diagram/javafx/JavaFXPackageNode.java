package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graphview.SmartShapeTypeSource;

import java.util.Objects;

public class JavaFXPackageNode extends JavaFXUMLNode {

    public JavaFXPackageNode(String name) {
        super(name);
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
}
