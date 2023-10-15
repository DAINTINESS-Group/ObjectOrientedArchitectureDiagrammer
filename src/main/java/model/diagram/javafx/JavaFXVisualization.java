package model.diagram.javafx;

import java.util.Collection;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

public interface JavaFXVisualization {

	/**
	 * This method is responsible for create the GraphView object that will be rendered in the front-end.
	 *
	 * @return the {@link SmartGraphPanel} created
	 */
	SmartGraphPanel<String, String> createGraphView();

	// TODO Add Javadoc
	Collection<Vertex<String>> getVertexCollection();

	// TODO Add Javadoc
	SmartGraphPanel<String, String> getLoadedGraph();
}
