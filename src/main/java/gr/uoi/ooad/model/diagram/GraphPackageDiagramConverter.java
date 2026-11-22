package gr.uoi.ooad.model.diagram;

import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.PackageVertex;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class GraphPackageDiagramConverter {

    private final Map<PackageVertex, Set<Arc<PackageVertex>>> adjacencyList = new HashMap<>();
    private final Set<PackageVertex> vertices;

    public GraphPackageDiagramConverter(Set<PackageVertex> vertices) {
        this.vertices = vertices;
    }

    public Map<PackageVertex, Set<Arc<PackageVertex>>> convertGraphToPackageDiagram() {
        for (PackageVertex vertex : vertices) {
            adjacencyList.putIfAbsent(vertex, new LinkedHashSet<>());
            for (Arc<PackageVertex> arc : vertex.getArcs()) {
                if (vertices.contains(arc.targetVertex())) {
                    adjacencyList.get(arc.sourceVertex()).add(arc);
                }
            }
        }

        return adjacencyList;
    }
}
