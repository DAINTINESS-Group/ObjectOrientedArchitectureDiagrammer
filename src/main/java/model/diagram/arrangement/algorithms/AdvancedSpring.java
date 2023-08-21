package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.graph.Graph;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.arrangement.geometry.GeometryNode;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;

import java.awt.Dimension;
import java.awt.geom.Point2D;

public class AdvancedSpring implements LayoutAlgorithm{

	private Graph<String, String> graph;
	final static int MIN_X_WINDOW_VALUE = 25;
	final static int MIN_Y_WINDOW_VALUE = 25;
	final static int GRAPH_X_SIZE = 1200;
	final static int GRAPH_Y_SIZE = 725;
	
	public AdvancedSpring() {
    }
	
	@Override
	public void setGraph(Graph<String, String> graph) {
		this.graph = graph;
	}
	
	@Override
	public DiagramGeometry arrangeDiagram() {
		DiagramGeometry diagramGeometry = new DiagramGeometry();
		AbstractLayout<String, String> layout = new SpringLayout2<>(graph);
        layout.setSize(new Dimension(GRAPH_X_SIZE, GRAPH_Y_SIZE));
        for (String vertex : graph.getVertices()) {
            Point2D coordinates = layout.apply(vertex);
            GeometryNode geometryNode = new GeometryNode(vertex);
            double x = coordinates.getX();
            double y = coordinates.getY();            
        	if (x < MIN_X_WINDOW_VALUE) {
        		x = MIN_X_WINDOW_VALUE;
        	}
        	if (y < MIN_Y_WINDOW_VALUE) {
        		y = MIN_Y_WINDOW_VALUE;
        	}
            diagramGeometry.addGeometry(geometryNode, x, y);        
        }
		return diagramGeometry;
	}
}
