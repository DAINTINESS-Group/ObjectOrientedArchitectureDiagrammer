package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.graph.Graph;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.arrangement.geometry.GeometryNode;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;

import edu.uci.ics.jung.visualization.VisualizationViewer;

public class AdvancedFruchtermanReingold implements LayoutAlgorithm{

	private Graph<String, String> graph;
	private final static int MIN_X_WINDOW_VALUE = 25;
	private final static int MIN_Y_WINDOW_VALUE = 25;
	// private final static int GRAPH_X_SIZE = 1200;
	// private final static int GRAPH_Y_SIZE = 725;
	
	public AdvancedFruchtermanReingold() {
    }
	
	@Override
	public void setGraph(Graph<String, String> graph) {
		this.graph = graph;
	}
	
	@Override
	public DiagramGeometry arrangeDiagram() {
		double maxXdistance = 0.0;
		double maxYdistance = 0.0;
		DiagramGeometry diagramGeometry = new DiagramGeometry();
		AbstractLayout<String, String> layout = new FRLayout2<>(graph);
        @SuppressWarnings("unused")
		VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(layout);
        // layout.setSize(new Dimension(GRAPH_X_SIZE, GRAPH_Y_SIZE));
        for (String vertex : graph.getVertices()) {
        	GeometryNode geometryNode = new GeometryNode(vertex);
        	double x = layout.getX(vertex);
        	double y = layout.getY(vertex);            
        	if (x < MIN_X_WINDOW_VALUE) {
        		double difference = MIN_X_WINDOW_VALUE - x;
        		if(difference > maxXdistance) {
        			maxXdistance = difference;
        		}
        	}
        	if (y < MIN_Y_WINDOW_VALUE) {
        		double difference = MIN_Y_WINDOW_VALUE - y;
        		if(difference > maxYdistance) {
        			maxYdistance = difference;
        		}
        	}
            diagramGeometry.addGeometry(geometryNode, x, y);
        }
        diagramGeometry.correctPositions(maxXdistance, maxYdistance);
        return diagramGeometry;
	}

}
