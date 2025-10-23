package gr.uoi.ooad.model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import gr.uoi.smartgraph.graphview.element.UMLEdgeElement;
import gr.uoi.smartgraph.graphview.element.UMLNodeElement;
import java.util.Collection;

public interface JavaFXVisualization {

    /**
     * This method is responsible for create the GraphView object that will be rendered in the
     * front-end.
     *
     * @return the {@link SmartGraphPanel} created
     */
    SmartGraphPanel<UMLNodeElement, UMLEdgeElement> createGraphView();

    // TODO Add Javadoc
    Collection<Vertex<UMLNodeElement>> getVertexCollection();

    // TODO Add Javadoc
    SmartGraphPanel<UMLNodeElement, UMLEdgeElement> getLoadedGraph();
}
