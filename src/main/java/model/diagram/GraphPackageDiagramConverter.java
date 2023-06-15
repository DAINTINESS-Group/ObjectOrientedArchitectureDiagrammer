package model.diagram;

import model.graph.Arc;
import model.graph.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphPackageDiagramConverter {

    private final Set<Vertex> vertices;
    private final Map<Vertex, Set<Arc<Vertex>>> adjacencyList;

    public GraphPackageDiagramConverter(Set<Vertex> vertices) {
        this.vertices = vertices;
        adjacencyList = new HashMap<>();
    }

    public Map<Vertex, Set<Arc<Vertex>>> convertGraphToPackageDiagram() {
        for (Vertex vertex: vertices) {
            adjacencyList.put(vertex, new HashSet<>());
            for (Arc<Vertex> arc: vertex.getArcs()) {
                if (!vertices.contains(arc.getTargetVertex())) {
                    continue;
                }
                adjacencyList.get(arc.getSourceVertex()).add(arc);
            }
        }
        return adjacencyList;
    }

}
