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
import java.util.*;

public class PackageDiagramArrangementManager implements DiagramArrangementManagerInterface {

    private final Graph<String, String> graph;
    private final PackageDiagram packageDiagram;

    public PackageDiagramArrangementManager(PackageDiagram packageDiagram) {
        this.packageDiagram = packageDiagram;
        graph = populatePackageGraphWithStrings();
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeGraphMLDiagram() {
    	Map<Integer, Pair<Double, Double>> nodesGeometryGraphML = new HashMap<>();
        Graph<Integer, String> graph = populatePackageGraph();
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        for (Integer i : packageDiagram.getGraphNodes().values()) {
        	nodesGeometryGraphML.put(i, new Pair<>(layout.getX(i), layout.getY(i)));
        }
        return nodesGeometryGraphML;
    }
    
    @Override
    public DiagramGeometry arrangeDiagram() {
    	LayoutAlgorithmFactory layoutAlgorithmFactory = new LayoutAlgorithmFactory();
    	LayoutAlgorithm sugiyama = layoutAlgorithmFactory.createLayoutAlgorithm(LayoutAlgorithmType.SUGIYAMA);
		sugiyama.setGraph(graph);
        return sugiyama.arrangeDiagram();
    }
    
    @Override
    public DiagramGeometry applyNewLayout(String algorithmType){
    	LayoutAlgorithmFactory layoutAlgorithmFactory = new LayoutAlgorithmFactory();
    	LayoutAlgorithmType algorithmEnumType = LayoutAlgorithmType.valueOf(algorithmType.toUpperCase());
    	LayoutAlgorithm layout = layoutAlgorithmFactory.createLayoutAlgorithm(algorithmEnumType);
    	layout.setGraph(graph);
    	return layout.arrangeDiagram();
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

    private Graph<String, String> populatePackageGraphWithStrings(){
        Graph<String, String> graph = new SparseGraph<>();
        for (PackageVertex vertex: packageDiagram.getGraphNodes().keySet()) {
        	graph.addVertex(vertex.getName());
        }

        List<Arc<PackageVertex>> arcs = new ArrayList<>();
        for (Set<Arc<PackageVertex>> arcSet: packageDiagram.getDiagram().values()) {
        	arcs.addAll(arcSet);
        }

        for (Arc<PackageVertex> arc: arcs) {
        	graph.addEdge(arc.getSourceVertex().getName() + " " + arc.getTargetVertex().getName(), arc.getSourceVertex().getName(), arc.getTargetVertex().getName(), EdgeType.DIRECTED);
        }

        return graph;
    }

}
