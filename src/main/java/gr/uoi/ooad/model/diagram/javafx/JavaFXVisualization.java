package gr.uoi.ooad.model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import gr.uoi.smartgraph.graphview.element.JavaFXUMLNode;

import java.util.Collection;

public interface JavaFXVisualization {

    /**
     * This method is responsible for create the GraphView object that will be rendered in the
     * front-end.
     *
     * @return the {@link SmartGraphPanel} created
     */
    SmartGraphPanel<JavaFXUMLNode, String> createGraphView();

    // TODO Add Javadoc
    Collection<Vertex<JavaFXUMLNode>> getVertexCollection();

    // TODO Add Javadoc
    SmartGraphPanel<JavaFXUMLNode, String> getLoadedGraph();
}
