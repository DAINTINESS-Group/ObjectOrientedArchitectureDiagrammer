package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.diagram.ClassDiagram;
import model.diagram.arrangement.algorithms.LayoutAlgorithm;
import model.diagram.arrangement.algorithms.LayoutAlgorithmFactory;
import model.diagram.arrangement.algorithms.LayoutAlgorithmType;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.javatuples.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassDiagramArrangementManager implements DiagramArrangementManagerInterface
{

    public static final LayoutAlgorithmType   LAYOUT_ALGORITHM_TYPE = LayoutAlgorithmType.SUGIYAMA;
    public static final int                   WIDTH                 = 1500;
    public static final int                   HEIGHT                = 1000;
    private final       ClassDiagram          classDiagram;
    private final       Graph<String, String> graph;


    public ClassDiagramArrangementManager(ClassDiagram classDiagram)
    {
        this.classDiagram = classDiagram;
        graph             = createGraphWithStrings();
    }


    @Override
    public Map<Integer, Pair<Double, Double>> arrangeGraphMLDiagram()
    {
        Map<Integer, Pair<Double, Double>> nodesGeometryGraphML = new HashMap<>();
        Graph<Integer, String>             graph                = createGraph();
        AbstractLayout<Integer, String>    layout               = new SpringLayout<>(graph);
        layout.setSize(new Dimension(WIDTH, HEIGHT));
        for (Integer i : classDiagram.getGraphNodes().values())
        {
            nodesGeometryGraphML.put(i, new Pair<>(layout.getX(i), layout.getY(i)));
        }

        return nodesGeometryGraphML;
    }


    @Override
    public DiagramGeometry arrangeDiagram()
    {
        LayoutAlgorithm layoutAlgorithm = LayoutAlgorithmFactory.createLayoutAlgorithm(LAYOUT_ALGORITHM_TYPE);
        layoutAlgorithm.setGraph(graph);
        return layoutAlgorithm.arrangeDiagram();
    }


    @Override
    public DiagramGeometry applyNewLayout(String algorithmType)
    {
        LayoutAlgorithmType algorithmEnumType = LayoutAlgorithmType.get(algorithmType);
        LayoutAlgorithm     layout            = LayoutAlgorithmFactory.createLayoutAlgorithm(algorithmEnumType);
        layout.setGraph(graph);
        return layout.arrangeDiagram();
    }


    private Graph<Integer, String> createGraph()
    {
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer nodeId : classDiagram.getGraphNodes().values())
        {
            graph.addVertex(nodeId);
        }

        List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
        for (Set<Arc<ClassifierVertex>> arcSet : classDiagram.getDiagram().values())
        {
            arcs.addAll(arcSet);
        }

        for (Arc<ClassifierVertex> arc : arcs)
        {
            graph.addEdge(classDiagram.getGraphNodes().get(arc.sourceVertex()) + " " + classDiagram.getGraphNodes().get(arc.targetVertex()),
                          classDiagram.getGraphNodes().get(arc.sourceVertex()),
                          classDiagram.getGraphNodes().get(arc.targetVertex()),
                          EdgeType.DIRECTED);
        }

        return graph;
    }


    private Graph<String, String> createGraphWithStrings()
    {
        Graph<String, String> graph = new SparseGraph<>();
        for (ClassifierVertex vertex : classDiagram.getGraphNodes().keySet())
        {
            graph.addVertex(vertex.getName());
        }

        List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
        for (Set<Arc<ClassifierVertex>> arcSet : classDiagram.getDiagram().values())
        {
            arcs.addAll(arcSet);
        }

        for (Arc<ClassifierVertex> arc : arcs)
        {
            graph.addEdge(arc.sourceVertex().getName() + " " + arc.targetVertex().getName(),
                          arc.sourceVertex().getName(),
                          arc.targetVertex().getName(), EdgeType.DIRECTED);
        }

        return graph;
    }

}