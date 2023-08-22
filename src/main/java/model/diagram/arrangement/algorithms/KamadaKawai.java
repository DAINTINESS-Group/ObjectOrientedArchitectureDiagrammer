package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.arrangement.geometry.GeometryNode;

import java.awt.Dimension;

public class KamadaKawai implements LayoutAlgorithm{

	private Graph<String, String> graph;
	private final static int MIN_X_WINDOW_VALUE = 25;
	private final static int MIN_Y_WINDOW_VALUE = 25;
	private final static int GRAPH_X_SIZE = 1200;
	private final static int GRAPH_Y_SIZE = 725;
	
	public KamadaKawai() {
    }
	
	@Override
	public void setGraph(Graph<String, String> graph) {
		this.graph = graph;
	}
	
	@Override
	public DiagramGeometry arrangeDiagram() {
		DiagramGeometry diagramGeometry = new DiagramGeometry();
		AbstractLayout<String, String> layout = new KKLayout<>(graph);
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
