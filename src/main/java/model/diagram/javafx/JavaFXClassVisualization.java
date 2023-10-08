package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.ClassDiagram;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.ClassifierVertex;
import model.graph.VertexType;

import java.util.Collection;
import java.util.Set;

public class JavaFXClassVisualization implements JavaFXVisualization {

	private final ClassDiagram 					  classDiagram;
	private 	  SmartGraphPanel<String, String> graphView;
	private 	  Collection<Vertex<String>> 	  vertexCollection;

	public JavaFXClassVisualization(ClassDiagram diagram) {
		classDiagram = diagram;
	}

	@Override
	public SmartGraphPanel<String, String> createGraphView() {
		Graph<String, String> graph = createGraph();
		vertexCollection = graph.vertices();
		graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
		setSinkVertexCustomStyle();
		return graphView;
	}

	@Override
	public Collection<Vertex<String>> getVertexCollection(){
		return vertexCollection;
	}

	private Graph<String, String> createGraph() {
		Digraph<String, String> directedGraph = new DigraphEdgeList<>();
		for (ClassifierVertex classifierVertex : classDiagram.getDiagram().keySet()) {
			directedGraph.insertVertex(classifierVertex.getName());
		}
		insertSinkVertexArcs(directedGraph);
		return directedGraph;
	}

	private void insertSinkVertexArcs(Digraph<String, String> directedGraph){
		for (Set<Arc<ClassifierVertex>> arcs : classDiagram.getDiagram().values()) {
			for (Arc<ClassifierVertex> arc: arcs) {
				if (arc.arcType().equals(ArcType.AGGREGATION)) {
					directedGraph.insertEdge(
						arc.targetVertex().getName(), arc.sourceVertex().getName(),
						arc.targetVertex().getName() + "_" + arc.sourceVertex().getName() + "_" + arc.arcType().toString().toLowerCase());
				}else {
					directedGraph.insertEdge(
						arc.sourceVertex().getName(), arc.targetVertex().getName(),
						arc.sourceVertex().getName() + "_" + arc.targetVertex().getName() + "_" + arc.arcType().toString().toLowerCase());
				}
			}
		}
	}

	private void setSinkVertexCustomStyle() {
		for (ClassifierVertex classifierVertex : classDiagram.getDiagram().keySet()){
			if (classifierVertex.getVertexType().equals(VertexType.INTERFACE)) {
				graphView.getStylableVertex(classifierVertex.getName()).setStyleClass("vertexInterface");
			}else {
				graphView.getStylableVertex(classifierVertex.getName()).setStyleClass("vertexPackage");
			}
		}
	}

	@Override
	public SmartGraphPanel<String, String> getLoadedGraph() {
		for (Vertex<String> vertex : vertexCollection) {
			for (ClassifierVertex classifierVertex: classDiagram.getDiagram().keySet()){
                if (!classifierVertex.getName().equals(vertex.element())) {
					continue;
				}
				graphView.setVertexPosition(
					vertex,
					classifierVertex.getCoordinates().getValue0(),
					classifierVertex.getCoordinates().getValue1()
				);
				break;
            }
		}
		return graphView;
	}

}
