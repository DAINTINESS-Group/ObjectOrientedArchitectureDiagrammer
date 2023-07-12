package model.diagram.arrangement.algorithms;

import java.util.Map;

import org.javatuples.Pair;

public interface LayoutAlgorithm {
	
    /**
     * This method is responsible for the arrangement of the graph by creating a graph of the Jung library
     * and using this graph's coordinates in our frontend
     *
     * @return a Map with the nodes' names as key and geometry(x,y) as value
     */
	Map<String, Pair<Double, Double>> arrangeDiagram();
}
