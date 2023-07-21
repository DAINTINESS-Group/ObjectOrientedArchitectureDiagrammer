package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;

import java.awt.Dimension;
import java.awt.geom.Point2D;

public class FruchtermanReingold implements LayoutAlgorithm{

	private Graph<String, String> graph;
	
	public FruchtermanReingold(Graph<String, String> graph) {
        this.graph = graph;
    }
	@Override
	public DiagramGeometry arrangeDiagram() {
		DiagramGeometry diagramGeometry = new DiagramGeometry();
		AbstractLayout<String, String> layout = new FRLayout<>(graph);
        layout.setSize(new Dimension(1200, 725));
        for (String vertex : graph.getVertices()) {
            Point2D coordinates = layout.apply(vertex);
            diagramGeometry.addGeometry(vertex, coordinates.getX(), coordinates.getY());
        }
		return diagramGeometry;
	}

}
