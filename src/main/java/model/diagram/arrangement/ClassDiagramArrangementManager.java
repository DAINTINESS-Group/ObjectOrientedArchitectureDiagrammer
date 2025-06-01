package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.diagram.ClassDiagram;
import model.diagram.arrangement.algorithms.LayoutAlgorithm;
import model.diagram.arrangement.algorithms.LayoutAlgorithmFactory;
import model.diagram.arrangement.algorithms.LayoutAlgorithmType;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.javatuples.Pair;

public class ClassDiagramArrangementManager implements DiagramArrangementManager {

    public static final LayoutAlgorithmType DEFAULT_LAYOUT_ALGORITHM = LayoutAlgorithmType.SUGIYAMA;
    public static final int HEIGHT = 1500;
    public static final int WIDTH = 1000;
    private final ClassDiagram classDiagram;

    public ClassDiagramArrangementManager(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeGraphMLDiagram() {
        Graph<Integer, String> graph = createGraph(classDiagram);
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(WIDTH, HEIGHT));

        return classDiagram.getGraphNodes().values().stream()
                .collect(
                        Collectors.toMap(
                                it -> it, it -> new Pair<>(layout.getX(it), layout.getY(it))));
    }

    @Override
    public DiagramGeometry arrangeDiagram() {
        LayoutAlgorithm layoutAlgorithm =
                LayoutAlgorithmFactory.createLayoutAlgorithm(DEFAULT_LAYOUT_ALGORITHM);
        return layoutAlgorithm.arrangeDiagram(createGraphWithStrings(classDiagram));
    }

    @Override
    public DiagramGeometry applyLayout(String algorithmType) {
        LayoutAlgorithmType algorithmEnumType = LayoutAlgorithmType.get(algorithmType);
        LayoutAlgorithm layout = LayoutAlgorithmFactory.createLayoutAlgorithm(algorithmEnumType);
        return layout.arrangeDiagram(createGraphWithStrings(classDiagram));
    }

    private static Graph<Integer, String> createGraph(ClassDiagram classDiagram) {
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer nodeId : classDiagram.getGraphNodes().values()) {
            graph.addVertex(nodeId);
        }

        List<Arc<ClassifierVertex>> arcs =
                classDiagram.getDiagram().values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(ArrayList::new));

        for (Arc<ClassifierVertex> arc : arcs) {
            graph.addEdge(
                    classDiagram.getGraphNodes().get(arc.sourceVertex())
                            + " "
                            + classDiagram.getGraphNodes().get(arc.targetVertex()),
                    classDiagram.getGraphNodes().get(arc.sourceVertex()),
                    classDiagram.getGraphNodes().get(arc.targetVertex()),
                    EdgeType.DIRECTED);
        }

        return graph;
    }

    private static Graph<String, String> createGraphWithStrings(ClassDiagram classDiagram) {
        Graph<String, String> graph = new SparseGraph<>();
        for (ClassifierVertex vertex : classDiagram.getGraphNodes().keySet()) {
            graph.addVertex(vertex.getName());
        }

        List<Arc<ClassifierVertex>> arcs =
                classDiagram.getDiagram().values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(ArrayList::new));

        for (Arc<ClassifierVertex> arc : arcs) {
            graph.addEdge(
                    arc.sourceVertex().getName() + " " + arc.targetVertex().getName(),
                    arc.sourceVertex().getName(),
                    arc.targetVertex().getName(),
                    EdgeType.DIRECTED);
        }

        return graph;
    }
}
