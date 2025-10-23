package gr.uoi.smartgraph.graphview.element;

import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;

public class UMLEdgeElementFactory {

    public static UMLEdgeElement createFromClassifierArc(Arc<ClassifierVertex> arc) {

        UMLEdgeElement edgeElement =
                new UMLEdgeElement(
                        arc.targetVertex().getName(), arc.sourceVertex().getName(), arc.arcType());

        return edgeElement;
    }

    public static UMLEdgeElement createFromPackageArc(Arc<PackageVertex> arc) {

        UMLEdgeElement edgeElement =
                new UMLEdgeElement(
                        arc.targetVertex().getName(), arc.sourceVertex().getName(), arc.arcType());

        return edgeElement;
    }
}
