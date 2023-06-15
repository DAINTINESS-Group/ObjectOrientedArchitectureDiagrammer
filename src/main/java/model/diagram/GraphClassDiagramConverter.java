package model.diagram;

import model.graph.Arc;
import model.graph.SinkVertex;

import java.util.*;

public class GraphClassDiagramConverter {

    private final Set<SinkVertex> sinkVertices;
    private final Map<SinkVertex, Set<Arc<SinkVertex>>> adjacencyList;

    public GraphClassDiagramConverter(Set<SinkVertex> sinkVertices) {
        this.sinkVertices = sinkVertices;
        adjacencyList = new HashMap<>();
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> convertGraphToClassDiagram() {
        for (SinkVertex sinkVertex: sinkVertices) {
            adjacencyList.put(sinkVertex, new HashSet<>());
            for (Arc<SinkVertex> arc: sinkVertex.getArcs()) {
                if (!sinkVertices.contains(arc.getTargetVertex())) {
                    continue;
                }
                adjacencyList.get(arc.getSourceVertex()).add(arc);
            }
        }
        return adjacencyList;
    }

}
