package model.diagram;

import model.graph.Arc;
import model.graph.ClassifierVertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphClassDiagramConverter {

	private final Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> adjacencyList;
	private final Set<ClassifierVertex> 							sinkVertices;

	public GraphClassDiagramConverter(Set<ClassifierVertex> sinkVertices) {
		this.sinkVertices = sinkVertices;
		adjacencyList = new HashMap<>();
	}

	public Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> convertGraphToClassDiagram() {
		for (ClassifierVertex classifierVertex : sinkVertices) {
			adjacencyList.put(classifierVertex, new HashSet<>());
			for (Arc<ClassifierVertex> arc: classifierVertex.getArcs()) {
				if (!sinkVertices.contains(arc.targetVertex())) {
					continue;
				}
				adjacencyList.get(arc.sourceVertex()).add(arc);
			}
		}
		return adjacencyList;
	}

}
