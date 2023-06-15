package model.diagram.arrangement;

import org.javatuples.Pair;

import java.util.Map;

public interface DiagramArrangement {

    /**
     * This method is responsible for the arrangement of the graph by creating a graph of the Jung library
     * and using the SpringLayout algorithm
     *
     * @return a Map with the nodes' id as key and geometry(x,y) as value
     */
    Map<Integer, Pair<Double, Double>> arrangeDiagram();

}
