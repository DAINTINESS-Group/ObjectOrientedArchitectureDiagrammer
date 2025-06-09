package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.util.Collection;

public interface JavaFXVisualization {

    /**
     * This method is responsible for create the GraphView object that will be rendered in the
     * front-end.
     *
     * @return the {@link SmartGraphPanel} created
     */
    SmartGraphPanel<String, String> createGraphView();

    // TODO Add Javadoc
    Collection<Vertex<String>> getVertexCollection();

    // TODO Add Javadoc
    SmartGraphPanel<String, String> getLoadedGraph();
}
