package gr.uoi.ooad.model.diagram.arrangement;

import edu.uci.ics.jung.graph.Graph;
import gr.uoi.ooad.model.diagram.arrangement.algorithms.LayoutAlgorithmType;
import gr.uoi.ooad.model.diagram.arrangement.geometry.DiagramGeometry;
import java.util.Map;
import org.javatuples.Pair;

public interface DiagramArrangementManager {

    /**
     * This method is responsible for the arrangement of the GraphML graph. It creates a {@link
     * Graph} graph using the SpringLayout algorithm.
     *
     * @return a Map with the nodes' ids as it keys and geometry(x,y) as it values.
     */
    Map<Integer, Pair<Double, Double>> arrangeGraphMLDiagram();

    /**
     * This method is responsible for the arrangement of the graph. It creates a {@link Graph} graph
     * using different algorithms, see {@link LayoutAlgorithmType}.
     *
     * @return a {@link DiagramGeometry} object that represents the diagram's geometry.
     */
    DiagramGeometry arrangeDiagram();

    // TODO JavaDoc
    DiagramGeometry applyLayout(String choice);
}
