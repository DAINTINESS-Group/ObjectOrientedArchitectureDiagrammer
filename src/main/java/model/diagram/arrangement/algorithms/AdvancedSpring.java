package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;

import java.awt.Dimension;
import java.awt.geom.Point2D;

public class AdvancedSpring implements LayoutAlgorithm{

	private Graph<String, String> graph;
	
	public AdvancedSpring(Graph<String, String> graph) {
        this.graph = graph;
    }
	@Override
	public DiagramGeometry arrangeDiagram() {
		DiagramGeometry diagramGeometry = new DiagramGeometry();
		AbstractLayout<String, String> layout = new SpringLayout2<>(graph);
        layout.setSize(new Dimension(1200, 725));
        for (String vertex : graph.getVertices()) {
            Point2D coordinates = layout.apply(vertex);
            diagramGeometry.addGeometry(vertex, coordinates.getX(), coordinates.getY());
        }
		return diagramGeometry;
	}
}
