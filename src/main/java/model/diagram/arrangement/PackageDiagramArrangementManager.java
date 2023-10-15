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

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PackageDiagramArrangementManager implements DiagramArrangementManagerInterface {

	public static final LayoutAlgorithmType   LAYOUT_ALGORITHM_TYPE = LayoutAlgorithmType.SUGIYAMA;
	private 	  final Graph<String, String> graph;
	private 	  final PackageDiagram 		  packageDiagram;

	public PackageDiagramArrangementManager(PackageDiagram packageDiagram) {
		this.packageDiagram = packageDiagram;
		this.graph = populatePackageGraphWithStrings();
	}

	@Override
	public Map<Integer, Pair<Double, Double>> arrangeGraphMLDiagram() {
		Map<Integer, Pair<Double, Double>> nodesGeometryGraphML = new HashMap<>();
		Graph<Integer, String> graph = populatePackageGraph();
		AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
		layout.setSize(new Dimension(1500, 1000));
		for (Integer i : this.packageDiagram.getGraphNodes().values()) {
			nodesGeometryGraphML.put(i, new Pair<>(layout.getX(i), layout.getY(i)));
		}
		return nodesGeometryGraphML;
	}

	@Override
	public DiagramGeometry arrangeDiagram() {
		LayoutAlgorithm layoutAlgorithm = LayoutAlgorithmFactory.createLayoutAlgorithm(LAYOUT_ALGORITHM_TYPE);
		layoutAlgorithm.setGraph(this.graph);
		return layoutAlgorithm.arrangeDiagram();
	}

	@Override
	public DiagramGeometry applyNewLayout(String algorithmType){
		LayoutAlgorithmType algorithmEnumType = LayoutAlgorithmType.valueOf(algorithmType.toUpperCase());
		LayoutAlgorithm layout = LayoutAlgorithmFactory.createLayoutAlgorithm(algorithmEnumType);
		layout.setGraph(this.graph);
		return layout.arrangeDiagram();
	}

	private Graph<Integer, String> populatePackageGraph() {
		Graph<Integer, String> graph = new SparseGraph<>();
		for (Integer i : this.packageDiagram.getGraphNodes().values()) {
			graph.addVertex(i);
		}

		List<Arc<PackageVertex>> arcs = new ArrayList<>();
		for (Set<Arc<PackageVertex>> arcSet: this.packageDiagram.getDiagram().values()) {
			arcs.addAll(arcSet);
		}

		for (Arc<PackageVertex> arc: arcs) {
			graph.addEdge(
				this.packageDiagram.getGraphNodes().get(arc.sourceVertex()) + " " + this.packageDiagram.getGraphNodes().get(arc.targetVertex()),
				this.packageDiagram.getGraphNodes().get(arc.sourceVertex()),
				this.packageDiagram.getGraphNodes().get(arc.targetVertex()), EdgeType.DIRECTED);
		}

		return graph;
	}

	private Graph<String, String> populatePackageGraphWithStrings(){
		Graph<String, String> graph = new SparseGraph<>();
		for (PackageVertex vertex: this.packageDiagram.getGraphNodes().keySet()) {
			graph.addVertex(vertex.getName());
		}

		List<Arc<PackageVertex>> arcs = new ArrayList<>();
		for (Set<Arc<PackageVertex>> arcSet: this.packageDiagram.getDiagram().values()) {
			arcs.addAll(arcSet);
		}

		for (Arc<PackageVertex> arc: arcs) {
			graph.addEdge(
				String.join(" ", arc.sourceVertex().getName(), arc.targetVertex().getName()),
				arc.sourceVertex().getName(),
				arc.targetVertex().getName(), EdgeType.DIRECTED);
		}

		return graph;
	}

}
