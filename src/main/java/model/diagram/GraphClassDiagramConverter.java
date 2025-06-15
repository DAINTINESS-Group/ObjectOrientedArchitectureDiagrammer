package model.diagram;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import model.graph.Arc;
import model.graph.ClassifierVertex;

public class GraphClassDiagramConverter {

    private final Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> adjacencyList = new HashMap<>();
    private final Set<ClassifierVertex> sinkVertices;

    public GraphClassDiagramConverter(Set<ClassifierVertex> sinkVertices) {
        this.sinkVertices = sinkVertices;
    }

    public Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> convertGraphToClassDiagram() {
        for (ClassifierVertex classifierVertex : sinkVertices) {
            adjacencyList.put(classifierVertex, new LinkedHashSet<>());
            for (Arc<ClassifierVertex> arc : classifierVertex.getArcs()) {
                if (sinkVertices.contains(arc.targetVertex())) {
                    adjacencyList.get(arc.sourceVertex()).add(arc);
                }
            }
        }

        return adjacencyList;
    }
}
