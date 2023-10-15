package model.diagram.arrangement;

import org.javatuples.Pair;

import model.diagram.arrangement.geometry.DiagramGeometry;

import java.util.Map;

public interface DiagramArrangementManagerInterface {

	/**
	 * This method is responsible for the arrangement of the graph by creating a graph of the Jung library
	 * and using the SpringLayout algorithm.
	 *
	 * @return a Map with the nodes' id as key and geometry(x,y) as value
	 */
	Map<Integer, Pair<Double, Double>> arrangeGraphMLDiagram();

	/**
	 * This method is responsible for the arrangement of the graph by creating a graph of the Jung library
	 * and using different layout algorithms.
	 *
	 * @return a {@link DiagramGeometry} object which represents the diagram's geometry
	 */
	DiagramGeometry arrangeDiagram();

	// TODO JavaDoc
	DiagramGeometry applyNewLayout(String choice);

}
