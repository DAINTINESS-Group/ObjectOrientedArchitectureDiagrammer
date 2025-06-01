package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.arrangement.geometry.GeometryNode;

public class AdvancedSpring implements LayoutAlgorithm {
    private static final int MIN_X_WINDOW_VALUE = 25;
    private static final int MIN_Y_WINDOW_VALUE = 25;

    @Override
    public DiagramGeometry arrangeDiagram(Graph<String, String> graph) {
        double maxXdistance = 0.0;
        double maxYdistance = 0.0;
        DiagramGeometry diagramGeometry = new DiagramGeometry();
        SpringLayout2<String, String> layout = new SpringLayout2<>(graph);
        layout.setForceMultiplier(0.1);
        layout.setRepulsionRange(500);
        @SuppressWarnings("unused")
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout);
        // layout.setSize(new Dimension(GRAPH_X_SIZE, GRAPH_Y_SIZE));
        for (String vertex : graph.getVertices()) {
            GeometryNode geometryNode = new GeometryNode(vertex);
            double x = layout.getX(vertex);
            double y = layout.getY(vertex);
            if (x < MIN_X_WINDOW_VALUE) {
                double difference = MIN_X_WINDOW_VALUE - x;
                maxXdistance = Math.max(maxXdistance, difference);
            }
            if (y < MIN_Y_WINDOW_VALUE) {
                double difference = MIN_Y_WINDOW_VALUE - y;
                maxYdistance = Math.max(maxYdistance, difference);
            }
            diagramGeometry.addGeometry(geometryNode, x, y);
        }
        diagramGeometry.correctPositions(maxXdistance, maxYdistance);
        return diagramGeometry;
    }
}
