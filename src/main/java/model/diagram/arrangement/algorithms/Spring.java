package model.diagram.arrangement.algorithms;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.arrangement.geometry.GeometryNode;


public class Spring implements LayoutAlgorithm
{
    private static final int    MIN_X_WINDOW_VALUE = 25;
    private static final int    MIN_Y_WINDOW_VALUE = 25;
    private static final double FORCE_MULTIPLIER   = 0.1;
    private static final int    REPULSION_RANGE    = 500;


    @Override
    public DiagramGeometry arrangeDiagram(Graph<String, String> graph)
    {
        double                       maxXdistance    = 0.0;
        double                       maxYdistance    = 0.0;
        DiagramGeometry              diagramGeometry = new DiagramGeometry();
        SpringLayout<String, String> layout          = new SpringLayout<>(graph);
        layout.setForceMultiplier(FORCE_MULTIPLIER);
        layout.setRepulsionRange(REPULSION_RANGE);
        // layout.setSize(new Dimension(GRAPH_X_SIZE, GRAPH_Y_SIZE));
        @SuppressWarnings("unused")
        VisualizationViewer<String, String> vv = new VisualizationViewer<>(layout);
        for (String vertex : graph.getVertices())
        {
            GeometryNode geometryNode = new GeometryNode(vertex);
            double       x            = layout.getX(vertex);
            double       y            = layout.getY(vertex);
            if (x < MIN_X_WINDOW_VALUE)
            {
                double difference = MIN_X_WINDOW_VALUE - x;
                maxXdistance      = Math.max(maxXdistance, difference);
            }
            if (y < MIN_Y_WINDOW_VALUE)
            {
                double difference = MIN_Y_WINDOW_VALUE - y;
                maxYdistance      = Math.max(maxYdistance, difference);
            }
            diagramGeometry.addGeometry(geometryNode, x, y);
        }
        diagramGeometry.correctPositions(maxXdistance, maxYdistance);
        return diagramGeometry;
    }

}
