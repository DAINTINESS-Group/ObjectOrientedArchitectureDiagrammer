package model.diagram.javafx;

import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import model.graph.VertexType;

public class JavaFXUMLNodeFactory {

    public static JavaFXUMLNode createClassifierNode(ClassifierVertex cVertex) {
        if (cVertex.getVertexType().equals(VertexType.INTERFACE)) {
            return new JavaFXInterfaceNode(cVertex.getName());
        }
        // FIXME: Discriminate a case for Enum
        return new JavaFXClassNode(cVertex.getName());
    }

    public static JavaFXUMLNode createPackageNode(PackageVertex pVertex) {
        return new JavaFXPackageNode(pVertex.getName());
    }
}
