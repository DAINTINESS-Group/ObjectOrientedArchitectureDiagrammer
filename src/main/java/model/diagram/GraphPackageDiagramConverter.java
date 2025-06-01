package model.diagram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.graph.Arc;
import model.graph.PackageVertex;

public class GraphPackageDiagramConverter {

    private final Map<PackageVertex, Set<Arc<PackageVertex>>> adjacencyList;
    private final Set<PackageVertex> vertices;

    public GraphPackageDiagramConverter(Set<PackageVertex> vertices) {
        this.vertices = vertices;
        adjacencyList = new HashMap<>();
    }

    public Map<PackageVertex, Set<Arc<PackageVertex>>> convertGraphToPackageDiagram() {
        for (PackageVertex vertex : vertices) {
            adjacencyList.put(vertex, new HashSet<>());
            for (Arc<PackageVertex> arc : vertex.getArcs()) {
                if (!vertices.contains(arc.targetVertex())) continue;

                adjacencyList.get(arc.sourceVertex()).add(arc);
            }
        }

        return adjacencyList;
    }
}
