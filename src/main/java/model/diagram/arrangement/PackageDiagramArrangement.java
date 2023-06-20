package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.diagram.PackageDiagram;
import model.graph.Arc;
import model.graph.PackageVertex;
import org.javatuples.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PackageDiagramArrangement implements DiagramArrangement {

    private final Map<Integer, Pair<Double, Double>> nodesGeometry;
    private final PackageDiagram packageDiagram;

    public PackageDiagramArrangement(PackageDiagram packageDiagram) {
        this.packageDiagram = packageDiagram;
        nodesGeometry = new HashMap<>();
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram() {
        Graph<Integer, String> graph = populatePackageGraph();
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        for (Integer i : packageDiagram.getGraphNodes().values()) {
            nodesGeometry.put(i, new Pair<>(layout.getX(i), layout.getY(i)));
        }
        return nodesGeometry;
    }

    private Graph<Integer, String> populatePackageGraph() {
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer i : packageDiagram.getGraphNodes().values()) {
            graph.addVertex(i);
        }

        List<Arc<PackageVertex>> arcs = new ArrayList<>();
        for (Set<Arc<PackageVertex>> arcSet: packageDiagram.getDiagram().values()) {
            arcs.addAll(arcSet);
        }

        for (Arc<PackageVertex> arc: arcs) {
            graph.addEdge(packageDiagram.getGraphNodes().get(arc.getSourceVertex()) + " " + packageDiagram.getGraphNodes().get(arc.getTargetVertex()),
                    packageDiagram.getGraphNodes().get(arc.getSourceVertex()), packageDiagram.getGraphNodes().get(arc.getTargetVertex()), EdgeType.DIRECTED);
        }

        return graph;
    }

}
