package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.arrangement.geometry.GeometryNode;

import java.awt.Dimension;

public class Spring implements LayoutAlgorithm{

	private Graph<String, String> graph;
	private final static int MIN_X_WINDOW_VALUE = 25;
	private final static int MIN_Y_WINDOW_VALUE = 25;
	private final static int GRAPH_X_SIZE = 1250;
	private final static int GRAPH_Y_SIZE = 725;
	
	public Spring() {
    }
	
	@Override
	public void setGraph(Graph<String, String> graph) {
		this.graph = graph;
	}
	
	@Override
	public DiagramGeometry arrangeDiagram() {
		DiagramGeometry diagramGeometry = new DiagramGeometry();
		SpringLayout<String, String> layout = new SpringLayout<>(graph);
		layout.setForceMultiplier(0.1);
		layout.setRepulsionRange(600);
		VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(layout);
		// layout.setSize(new Dimension(GRAPH_X_SIZE, GRAPH_Y_SIZE));
        for (String vertex : graph.getVertices()) {
            GeometryNode geometryNode = new GeometryNode(vertex);
            double x = layout.getX(vertex);
            double y = layout.getY(vertex);            
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
