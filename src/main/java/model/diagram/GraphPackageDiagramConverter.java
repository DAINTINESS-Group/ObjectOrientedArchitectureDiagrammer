package model.diagram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.graph.Arc;
import model.graph.PackageVertex;

public class GraphPackageDiagramConverter {

    private final Map<PackageVertex, Set<Arc<PackageVertex>>> adjacencyList = new HashMap<>();
    private final Set<PackageVertex> vertices;

    public GraphPackageDiagramConverter(Set<PackageVertex> vertices) {
        this.vertices = vertices;
    }

    public Map<PackageVertex, Set<Arc<PackageVertex>>> convertGraphToPackageDiagram() {
        for (PackageVertex vertex : vertices) {
            adjacencyList.putIfAbsent(vertex, new HashSet<>());
            for (Arc<PackageVertex> arc : vertex.getArcs()) {
                if (vertices.contains(arc.targetVertex())) {
                    adjacencyList.get(arc.sourceVertex()).add(arc);
                }
            }
        }

        return adjacencyList;
    }
}
