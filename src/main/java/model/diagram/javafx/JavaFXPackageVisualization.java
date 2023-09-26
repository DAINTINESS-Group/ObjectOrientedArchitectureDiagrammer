package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.PackageDiagram;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.PackageVertex;
import model.graph.VertexType;

import java.util.Collection;
import java.util.Set;

public class JavaFXPackageVisualization implements JavaFXVisualization {

	private SmartGraphPanel<String, String> graphView;
	private final PackageDiagram packageDiagram;
	private Collection<Vertex<String>> vertexCollection;

	public JavaFXPackageVisualization(PackageDiagram diagram) {
		packageDiagram = diagram;
	}

	@Override
	public SmartGraphPanel<String, String> createGraphView() {
		Graph<String, String> graph = createGraph();
		vertexCollection = graph.vertices();
		graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
		setVertexCustomStyle();
		return graphView;
	}

	@Override
	public Collection<Vertex<String>> getVertexCollection(){
		return vertexCollection;
	}

	private Graph<String, String> createGraph() {
		Digraph<String, String> directedGraph = new DigraphEdgeList<>();
		for (PackageVertex vertex: packageDiagram.getDiagram().keySet()) {
			if(vertex.getSinkVertices().size() > 0) {
				directedGraph.insertVertex(vertex.getName());
			}
		}
		insertVertexArcs(directedGraph);
		return directedGraph;
	}

	private void insertVertexArcs(Digraph<String, String> directedGraph){
		for (Set<Arc<PackageVertex>> arcs : packageDiagram.getDiagram().values()) {
			for (Arc<PackageVertex> arc: arcs) {
				if (arc.getArcType().equals(ArcType.AGGREGATION)) {
					directedGraph.insertEdge(arc.getTargetVertex().getName(), arc.getSourceVertex().getName(),
							arc.getTargetVertex().getName() + "_" + arc.getSourceVertex().getName() + "_" + arc.getArcType().toString().toLowerCase());
				}else {
					directedGraph.insertEdge(arc.getSourceVertex().getName(), arc.getTargetVertex().getName(),
							arc.getSourceVertex().getName() + "_" +arc.getTargetVertex().getName() + "_" + arc.getArcType().toString().toLowerCase());
				}
			}
		}
	}

	private void setVertexCustomStyle() {
		for (PackageVertex vertex: packageDiagram.getDiagram().keySet()){
			if (vertex.getVertexType().equals(VertexType.INTERFACE)) {
				graphView.getStylableVertex(vertex.getName()).setStyleClass("vertexInterface");
			}else {
				if(vertex.getSinkVertices().size() > 0) {
					graphView.getStylableVertex(vertex.getName()).setStyleClass("vertexPackage");
				}
			}
		}
	}

	@Override
	public SmartGraphPanel<String, String> getLoadedGraph() {
		for (Vertex<String> vertex : vertexCollection) {
			for (PackageVertex packageVertex: packageDiagram.getDiagram().keySet()){
				if(packageVertex.getName().equals(vertex.element())) {
					graphView.setVertexPosition(vertex, packageVertex.getCoordinates().getValue0(), packageVertex.getCoordinates().getValue1());
					break;
				}
			}
		}
		return graphView;
	}
}
