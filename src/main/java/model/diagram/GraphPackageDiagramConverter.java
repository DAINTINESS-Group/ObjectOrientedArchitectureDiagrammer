package model.diagram;

import model.graph.Arc;
import model.graph.PackageVertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphPackageDiagramConverter {

	private final Map<PackageVertex, Set<Arc<PackageVertex>>> adjacencyList;
	private final Set<PackageVertex> 						  vertices;

	public GraphPackageDiagramConverter(Set<PackageVertex> vertices) {
		this.vertices = vertices;
		this.adjacencyList = new HashMap<>();
	}

	public Map<PackageVertex, Set<Arc<PackageVertex>>> convertGraphToPackageDiagram() {
		for (PackageVertex vertex: this.vertices) {
			this.adjacencyList.put(vertex, new HashSet<>());
			for (Arc<PackageVertex> arc: vertex.getArcs()) {
				if (!this.vertices.contains(arc.targetVertex())) {
					continue;
				}
				this.adjacencyList.get(arc.sourceVertex()).add(arc);
			}
		}
		return this.adjacencyList;
	}

}
