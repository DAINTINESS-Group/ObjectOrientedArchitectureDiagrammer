package model.diagram.arrangement.algorithms;

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
		double maxXdistance = 0.0;
		double maxYdistance = 0.0;
		DiagramGeometry diagramGeometry = new DiagramGeometry();
		KKLayout<String, String> layout = new KKLayout<>(graph);
		layout.setSize(new Dimension(GRAPH_X_SIZE, GRAPH_Y_SIZE));
		layout.setLengthFactor(1.5);
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
