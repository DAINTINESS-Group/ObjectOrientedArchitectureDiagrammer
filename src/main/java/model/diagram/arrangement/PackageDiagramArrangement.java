package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import model.diagram.PackageDiagram;
import model.diagram.arrangement.algorithms.AdvancedFruchtermanReingold;
import model.diagram.arrangement.algorithms.AdvancedSpring;
import model.diagram.arrangement.algorithms.DiagramGeometry;
import model.diagram.arrangement.algorithms.FruchtermanReingold;
import model.diagram.arrangement.algorithms.KamadaKawai;
import model.diagram.arrangement.algorithms.LayoutAlgorithm;
import model.diagram.arrangement.algorithms.Spring;
import model.diagram.arrangement.algorithms.Sugiyama;
import model.graph.Arc;
import model.graph.PackageVertex;
import org.javatuples.Pair;

import java.awt.Dimension;
import java.util.*;

public class PackageDiagramArrangement implements DiagramArrangement {

    private final Graph<String, String> graph;
    private final PackageDiagram packageDiagram;

    public PackageDiagramArrangement(PackageDiagram packageDiagram) {
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
    	LayoutAlgorithm sugiyama = new Sugiyama(graph);
        return sugiyama.arrangeDiagram();
    }
    
    @Override
    public DiagramGeometry applyNewLayout(String choice){
    	LayoutAlgorithm layout;
    	switch (choice) {
    		case "spring":
    			layout = new Spring(graph);
    			return layout.arrangeDiagram();
    		case "spring2":
    			layout = new AdvancedSpring(graph);
    			return layout.arrangeDiagram();
    		case "fr":
    			layout = new FruchtermanReingold(graph);
    			return layout.arrangeDiagram();
    		case "fr2":
    			layout = new AdvancedFruchtermanReingold(graph);
    			return layout.arrangeDiagram();
    		case "kk":
    			layout = new KamadaKawai(graph);
    			return layout.arrangeDiagram();
    		default:
    			layout = new Sugiyama(graph);
    	        return layout.arrangeDiagram();	
    	}
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
