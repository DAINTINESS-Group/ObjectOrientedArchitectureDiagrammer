package gr.uoi.smartgraph.graphview.element;

import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;
import gr.uoi.ooad.model.graph.VertexType;

public class UMLNodeElementFactory {

    public static UMLNodeElement createClassifierNode(ClassifierVertex cVertex) {
        if (cVertex.getVertexType().equals(VertexType.INTERFACE)) {
            return new InterfaceNodeElement(cVertex.getName());
        }
        // FIXME: Discriminate a case for Enum
        return new ClassNodeElement(cVertex.getName());
    }

    public static UMLNodeElement createPackageNode(PackageVertex pVertex) {
        return new PackageNodeElement(pVertex.getName());
    }
}
