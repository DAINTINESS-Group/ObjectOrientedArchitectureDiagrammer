package model.diagram;

import model.graph.Arc;
import model.graph.ClassifierVertex;

import java.util.*;

public class GraphClassDiagramConverter {

	private final Set<ClassifierVertex> sinkVertices;
	private final Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> adjacencyList;

	public GraphClassDiagramConverter(Set<ClassifierVertex> sinkVertices) {
		this.sinkVertices = sinkVertices;
		adjacencyList = new HashMap<>();
	}

	public Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> convertGraphToClassDiagram() {
		for (ClassifierVertex classifierVertex : sinkVertices) {
			adjacencyList.put(classifierVertex, new HashSet<>());
			for (Arc<ClassifierVertex> arc: classifierVertex.getArcs()) {
				if (!sinkVertices.contains(arc.getTargetVertex())) {
					continue;
				}
				adjacencyList.get(arc.getSourceVertex()).add(arc);
			}
		}
		return adjacencyList;
	}

}
