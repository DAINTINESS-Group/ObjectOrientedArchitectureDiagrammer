package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.graph.Graph;

public interface LayoutAlgorithm {
	
    /**
     * This method is responsible for the arrangement of the graph by creating a graph of the Jung library
     * and using this graph's coordinates in our frontend
     *
     * @return a Map with the nodes' names as key and geometry(x,y) as value
     */
	DiagramGeometry arrangeDiagram();

	void setGraph(Graph<String, String> graph);
}
