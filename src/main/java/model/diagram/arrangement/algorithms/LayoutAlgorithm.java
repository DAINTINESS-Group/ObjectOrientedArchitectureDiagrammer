package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.graph.Graph;
import model.diagram.arrangement.geometry.DiagramGeometry;

public interface LayoutAlgorithm {
	// TODO Update JavaDoc
	/**
	 * This method is responsible for the arrangement of the graph by creating a graph of the Jung library
	 * and using this graph's coordinates in our front end.
	 *
	 * @return a {@link DiagramGeometry} object which represents the diagram's geometry
	 */
	DiagramGeometry arrangeDiagram();

	// TODO JavaDoc
	void setGraph(Graph<String, String> graph);
}
