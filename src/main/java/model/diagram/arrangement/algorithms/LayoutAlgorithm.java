package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.graph.Graph;
import model.diagram.arrangement.geometry.DiagramGeometry;

public interface LayoutAlgorithm {
	public final static int MIN_X_WINDOW_VALUE = 25;
	public final static int MIN_Y_WINDOW_VALUE = 25;

	/**
	 * This method is responsible for the arrangement of the graph by creating a graph of the Jung library
	 * and using this graph's coordinates in our front end
	 *
	 * @return a Map with the nodes' names as key and geometry(x,y) as value
	 */
	DiagramGeometry arrangeDiagram();

	// TODO JavaDoc
	void setGraph(Graph<String, String> graph);
}
