package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.diagram.PackageDiagram;
import model.diagram.arrangement.algorithms.LayoutAlgorithm;
import model.diagram.arrangement.algorithms.LayoutAlgorithmFactory;
import model.diagram.arrangement.algorithms.LayoutAlgorithmType;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.graph.Arc;
import model.graph.PackageVertex;
import org.javatuples.Pair;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PackageDiagramArrangementManager implements DiagramArrangementManager
{

    public static final LayoutAlgorithmType   LAYOUT_ALGORITHM_TYPE = LayoutAlgorithmType.SUGIYAMA;
    public static final int                   WIDTH                 = 1500;
    public static final int                   HEIGHT                = 1000;
    private final       Graph<String, String> graph;
    private final       PackageDiagram        packageDiagram;


    public PackageDiagramArrangementManager(PackageDiagram packageDiagram)
    {
        this.packageDiagram = packageDiagram;
        graph               = populatePackageGraphWithStrings();
    }


    @Override
    public Map<Integer, Pair<Double, Double>> arrangeGraphMLDiagram()
    {
        Map<Integer, Pair<Double, Double>> nodesGeometryGraphML = new HashMap<>();
        Graph<Integer, String>             graph                = populatePackageGraph();
        AbstractLayout<Integer, String>    layout               = new SpringLayout<>(graph);
        layout.setSize(new Dimension(WIDTH, HEIGHT));
        for (Integer i : packageDiagram.getGraphNodes().values())
        {
            nodesGeometryGraphML.put(i, new Pair<>(layout.getX(i), layout.getY(i)));
        }

        return nodesGeometryGraphML;
    }


    @Override
    public DiagramGeometry arrangeDiagram()
    {
        LayoutAlgorithm layoutAlgorithm = LayoutAlgorithmFactory.createLayoutAlgorithm(LAYOUT_ALGORITHM_TYPE);
        return layoutAlgorithm.arrangeDiagram(graph);
    }


    @Override
    public DiagramGeometry applyLayout(String algorithmType)
    {
        LayoutAlgorithmType algorithmEnumType = LayoutAlgorithmType.get(algorithmType);
        LayoutAlgorithm     layout            = LayoutAlgorithmFactory.createLayoutAlgorithm(algorithmEnumType);
        return layout.arrangeDiagram(graph);
    }


    private Graph<Integer, String> populatePackageGraph()
    {
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer i : packageDiagram.getGraphNodes().values())
        {
            graph.addVertex(i);
        }

        List<Arc<PackageVertex>> arcs = new ArrayList<>();
        for (Set<Arc<PackageVertex>> arcSet : packageDiagram.getDiagram().values())
        {
            arcs.addAll(arcSet);
        }

        for (Arc<PackageVertex> arc : arcs)
        {
            graph.addEdge(packageDiagram.getGraphNodes().get(arc.sourceVertex()) + " " + packageDiagram.getGraphNodes().get(arc.targetVertex()),
                          packageDiagram.getGraphNodes().get(arc.sourceVertex()),
                          packageDiagram.getGraphNodes().get(arc.targetVertex()), EdgeType.DIRECTED);
        }

        return graph;
    }


    private Graph<String, String> populatePackageGraphWithStrings()
    {
        Graph<String, String> graph = new SparseGraph<>();
        for (PackageVertex vertex : packageDiagram.getGraphNodes().keySet())
        {
            graph.addVertex(vertex.getName());
        }

        List<Arc<PackageVertex>> arcs = new ArrayList<>();
        for (Set<Arc<PackageVertex>> arcSet : packageDiagram.getDiagram().values())
        {
            arcs.addAll(arcSet);
        }

        for (Arc<PackageVertex> arc : arcs)
        {
            graph.addEdge(String.join(" ",
                                      arc.sourceVertex().getName(),
                                      arc.targetVertex().getName()),
                          arc.sourceVertex().getName(),
                          arc.targetVertex().getName(), EdgeType.DIRECTED);
        }

        return graph;
    }

}
