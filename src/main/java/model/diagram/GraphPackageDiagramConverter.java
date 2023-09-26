package model.diagram;

import model.graph.Arc;
import model.graph.PackageVertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphPackageDiagramConverter {

	private final Set<PackageVertex> vertices;
	private final Map<PackageVertex, Set<Arc<PackageVertex>>> adjacencyList;

	public GraphPackageDiagramConverter(Set<PackageVertex> vertices) {
		this.vertices = vertices;
		adjacencyList = new HashMap<>();
	}

	public Map<PackageVertex, Set<Arc<PackageVertex>>> convertGraphToPackageDiagram() {
		for (PackageVertex vertex: vertices) {
			adjacencyList.put(vertex, new HashSet<>());
			for (Arc<PackageVertex> arc: vertex.getArcs()) {
				if (!vertices.contains(arc.getTargetVertex())) {
					continue;
				}
				adjacencyList.get(arc.getSourceVertex()).add(arc);
			}
		}
		return adjacencyList;
	}

}
