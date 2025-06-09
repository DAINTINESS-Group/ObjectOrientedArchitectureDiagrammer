package model.diagram.arrangement.algorithms;

import de.odysseus.ithaka.digraph.SimpleDigraph;
import de.odysseus.ithaka.digraph.SimpleDigraphAdapter;
import de.odysseus.ithaka.digraph.layout.DigraphLayout;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutDimension;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutDimensionProvider;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutNode;
import de.odysseus.ithaka.digraph.layout.DigrpahLayoutBuilder;
import de.odysseus.ithaka.digraph.layout.sugiyama.SugiyamaBuilder;
import edu.uci.ics.jung.graph.Graph;
import java.util.HashMap;
import java.util.Map;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.arrangement.geometry.GeometryNode;

public class Sugiyama implements LayoutAlgorithm {
    private static final int MIN_X_WINDOW_VALUE = 25;
    private static final int MIN_Y_WINDOW_VALUE = 25;
    private static final int VERTEX_X_SIZE = 20;
    private static final int VERTEX_Y_SIZE = 20;
    private static final int HORIZONTAL_SPACING = 125;
    private static final int VERTICAL_SPACING = 50;

    private final Map<String, Integer> verticesMap;
    private SimpleDigraph<Integer> digraph;

    public Sugiyama() {
        this.verticesMap = new HashMap<>();
    }

    @Override
    public DiagramGeometry arrangeDiagram(Graph<String, String> graph) {
        double maxXdistance = 0.0;
        double maxYdistance = 0.0;
        DiagramGeometry diagramGeometry = new DiagramGeometry();
        digraph = new SimpleDigraphAdapter<>();
        fillVertexMap(graph);
        fillNeighboursMap(graph);
        DigraphLayoutDimensionProvider<Integer> dimensionProvider =
                node -> {
                    // We use OOAD vertices' size, in order to evaluate vertices coordinates.
                    return new DigraphLayoutDimension(VERTEX_X_SIZE, VERTEX_Y_SIZE);
                };
        DigrpahLayoutBuilder<Integer, Boolean> builder =
                new SugiyamaBuilder<>(HORIZONTAL_SPACING, VERTICAL_SPACING);
        DigraphLayout<Integer, Boolean> layout = builder.build(digraph, dimensionProvider);
        for (DigraphLayoutNode<Integer> vertex : layout.getLayoutGraph().vertices()) {
            for (Map.Entry<String, Integer> entryVertex : verticesMap.entrySet()) {
                if (!entryVertex.getValue().equals(vertex.getVertex())) continue;

                double x = vertex.getPoint().x;
                double y = vertex.getPoint().y;
                GeometryNode geometryNode = new GeometryNode(entryVertex.getKey());
                if (vertex.getPoint().x < MIN_X_WINDOW_VALUE) {
                    double difference = MIN_X_WINDOW_VALUE - x;
                    maxXdistance = Math.max(difference, maxXdistance);
                }
                if (vertex.getPoint().y < MIN_Y_WINDOW_VALUE) {
                    double difference = MIN_Y_WINDOW_VALUE - y;
                    maxYdistance = Math.max(difference, maxYdistance);
                }
                diagramGeometry.addGeometry(geometryNode, x, y);
                break;
            }
        }
        diagramGeometry.correctPositions(maxXdistance, maxYdistance);
        return diagramGeometry;
    }

    private void fillNeighboursMap(Graph<String, String> graph) {
        for (String edge : graph.getEdges()) {
            String[] vertices = edge.split(" ");
            digraph.add(verticesMap.get(vertices[0]), verticesMap.get(vertices[1]));
        }
    }

    private void fillVertexMap(Graph<String, String> graph) {
        int counter = 1;
        for (String vertex : graph.getVertices()) {
            verticesMap.put(vertex, counter++);
        }
    }
}
