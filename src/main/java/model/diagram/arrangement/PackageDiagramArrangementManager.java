package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.diagram.PackageDiagram;
import model.diagram.arrangement.algorithms.LayoutAlgorithm;
import model.diagram.arrangement.algorithms.LayoutAlgorithmFactory;
import model.diagram.arrangement.algorithms.LayoutAlgorithmType;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.graph.Arc;
import model.graph.PackageVertex;
import org.javatuples.Pair;

public class PackageDiagramArrangementManager implements DiagramArrangementManager {

    public static final LayoutAlgorithmType LAYOUT_ALGORITHM_TYPE = LayoutAlgorithmType.SUGIYAMA;
    public static final int WIDTH = 1500;
    public static final int HEIGHT = 1000;
    private final PackageDiagram packageDiagram;

    public PackageDiagramArrangementManager(PackageDiagram packageDiagram) {
        this.packageDiagram = packageDiagram;
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeGraphMLDiagram() {
        Graph<Integer, String> graph = populatePackageGraph();
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(WIDTH, HEIGHT));

        return packageDiagram.getGraphNodes().values().stream()
                .collect(
                        Collectors.toMap(
                                it -> it, it -> new Pair<>(layout.getX(it), layout.getY(it))));
    }

    @Override
    public DiagramGeometry arrangeDiagram() {
        LayoutAlgorithm layoutAlgorithm =
                LayoutAlgorithmFactory.createLayoutAlgorithm(LAYOUT_ALGORITHM_TYPE);
        return layoutAlgorithm.arrangeDiagram(populatePackageGraphWithStrings());
    }

    @Override
    public DiagramGeometry applyLayout(String algorithmType) {
        LayoutAlgorithmType algorithmEnumType = LayoutAlgorithmType.get(algorithmType);
        LayoutAlgorithm layout = LayoutAlgorithmFactory.createLayoutAlgorithm(algorithmEnumType);
        return layout.arrangeDiagram(populatePackageGraphWithStrings());
    }

    private Graph<Integer, String> populatePackageGraph() {
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer i : packageDiagram.getGraphNodes().values()) {
            graph.addVertex(i);
        }

        List<Arc<PackageVertex>> arcs =
                packageDiagram.getDiagram().values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(ArrayList::new));

        for (Arc<PackageVertex> arc : arcs) {
            graph.addEdge(
                    packageDiagram.getGraphNodes().get(arc.sourceVertex())
                            + " "
                            + packageDiagram.getGraphNodes().get(arc.targetVertex()),
                    packageDiagram.getGraphNodes().get(arc.sourceVertex()),
                    packageDiagram.getGraphNodes().get(arc.targetVertex()),
                    EdgeType.DIRECTED);
        }

        return graph;
    }

    private Graph<String, String> populatePackageGraphWithStrings() {
        Graph<String, String> graph = new SparseGraph<>();
        for (PackageVertex vertex : packageDiagram.getGraphNodes().keySet()) {
            graph.addVertex(vertex.getName());
        }

        List<Arc<PackageVertex>> arcs =
                packageDiagram.getDiagram().values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(ArrayList::new));

        for (Arc<PackageVertex> arc : arcs) {
            graph.addEdge(
                    String.join(" ", arc.sourceVertex().getName(), arc.targetVertex().getName()),
                    arc.sourceVertex().getName(),
                    arc.targetVertex().getName(),
                    EdgeType.DIRECTED);
        }

        return graph;
    }
}
