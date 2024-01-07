package model.diagram.arrangement.algorithms;

import java.util.HashMap;
import java.util.Map;

import de.odysseus.ithaka.digraph.SimpleDigraph;
import de.odysseus.ithaka.digraph.SimpleDigraphAdapter;
import de.odysseus.ithaka.digraph.layout.DigraphLayout;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutDimension;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutDimensionProvider;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutNode;
import de.odysseus.ithaka.digraph.layout.DigrpahLayoutBuilder;
import de.odysseus.ithaka.digraph.layout.sugiyama.SugiyamaBuilder;
import edu.uci.ics.jung.graph.Graph;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.arrangement.geometry.GeometryNode;

public class Sugiyama implements LayoutAlgorithm
{
    private final static int MIN_X_WINDOW_VALUE = 25;
    private final static int MIN_Y_WINDOW_VALUE = 25;
    private final static int VERTEX_X_SIZE      = 20;
    private final static int VERTEX_Y_SIZE      = 20;
    private final static int HORIZONTAL_SPACING = 125;
    private final static int VERTICAL_SPACING   = 50;

    private final Map<String, Integer>   verticesMap;
    private       Graph<String, String>  graph;
    private       SimpleDigraph<Integer> digraph;


    public Sugiyama()
    {
        this.verticesMap = new HashMap<>();
    }


    @Override
    public void setGraph(Graph<String, String> graph)
    {
        this.graph = graph;
    }


    @Override
    public DiagramGeometry arrangeDiagram()
    {
        double          maxXdistance    = 0.0;
        double          maxYdistance    = 0.0;
        DiagramGeometry diagramGeometry = new DiagramGeometry();
        digraph = new SimpleDigraphAdapter<>();
        fillVertexMap();
        fillNeighboursMap();
        DigraphLayoutDimensionProvider<Integer> dimensionProvider = node -> {
            // We use OOAD vertices' size, in order to evaluate vertices coordinates based on our vertices sizes.
            return new DigraphLayoutDimension(VERTEX_X_SIZE, VERTEX_Y_SIZE);
        };
        DigrpahLayoutBuilder<Integer, Boolean> builder = new SugiyamaBuilder<>(HORIZONTAL_SPACING, VERTICAL_SPACING);
        DigraphLayout<Integer, Boolean>        layout  = builder.build(digraph, dimensionProvider);
        for (DigraphLayoutNode<Integer> vertex : layout.getLayoutGraph().vertices())
        {
            for (Map.Entry<String, Integer> entryVertex : verticesMap.entrySet())
            {
                if (entryVertex.getValue().equals(vertex.getVertex()))
                {
                    double       x            = vertex.getPoint().x;
                    double       y            = vertex.getPoint().y;
                    GeometryNode geometryNode = new GeometryNode(entryVertex.getKey());
                    if (vertex.getPoint().x < MIN_X_WINDOW_VALUE)
                    {
                        double difference = MIN_X_WINDOW_VALUE - x;
                        if (difference > maxXdistance)
                        {
                            maxXdistance = difference;
                        }
                    }
                    if (vertex.getPoint().y < MIN_Y_WINDOW_VALUE)
                    {
                        double difference = MIN_Y_WINDOW_VALUE - y;
                        if (difference > maxYdistance)
                        {
                            maxYdistance = difference;
                        }
                    }
                    diagramGeometry.addGeometry(geometryNode, x, y);
                    break;
                }
            }
        }
        diagramGeometry.correctPositions(maxXdistance, maxYdistance);
        return diagramGeometry;
    }


    private void fillNeighboursMap()
    {
        for (String edge : graph.getEdges())
        {
            String[] vertices = edge.split(" ");
            digraph.add(verticesMap.get(vertices[0]), verticesMap.get(vertices[1]));
        }
    }


    private void fillVertexMap()
    {
        int counter = 1;
        for (String vertex : graph.getVertices())
        {
            verticesMap.put(vertex, counter);
            counter += 1;
        }
    }

}
