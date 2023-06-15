package model.diagram;

import model.graph.Arc;
import model.graph.ArcType;
import model.graph.SinkVertex;

import java.util.*;

public class ShadowCleaner {

    private final Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;
    private final List<ArcType> strongerToWeakerArcTypes;

    public ShadowCleaner(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        this.diagram = diagram;
        strongerToWeakerArcTypes = List.of(ArcType.EXTENSION, ArcType.IMPLEMENTATION, ArcType.AGGREGATION, ArcType.ASSOCIATION);
    }

    public Map<SinkVertex, Set<Arc<SinkVertex>>> shadowWeakRelationships() {
        for (Set<Arc<SinkVertex>> arcs: diagram.values()) {
            Map<SinkVertex, List<Arc<SinkVertex>>> shadowedArcs = new HashMap<>();
            for (Arc<SinkVertex> arc: arcs) {
                shadowedArcs.computeIfAbsent(arc.getTargetVertex(), sinkVertex -> new ArrayList<>()).add(arc);
            }

            for (Map.Entry<SinkVertex, List<Arc<SinkVertex>>> arc: shadowedArcs.entrySet()) {
                if (!doWeakRelationshipsExist(arc)) {
                    continue;
                }
                for (ArcType arcType: strongerToWeakerArcTypes) {
                    if (doesStrongerRelationshipExist(arc.getValue(), arcType)) {
                        removeWeakerRelationships(arcs, arc.getKey(), arcType);
                        break;
                    }
                }
            }
        }
        return diagram;
    }

    private boolean doWeakRelationshipsExist(Map.Entry<SinkVertex, List<Arc<SinkVertex>>> arc) {
        return arc.getValue().size() > 1;
    }

    private boolean doesStrongerRelationshipExist(List<Arc<SinkVertex>> arc, ArcType arcType) {
        Optional<Arc<SinkVertex>> inheritanceArc = arc.stream().filter(sinkVertexArc -> sinkVertexArc.getArcType().equals(arcType)).findFirst();
        return inheritanceArc.isPresent();
    }

    private void removeWeakerRelationships(Set<Arc<SinkVertex>> arcs, SinkVertex sinkVertex, ArcType arcType) {
        arcs.removeIf(arc -> arc.getTargetVertex().equals(sinkVertex) && !arc.getArcType().equals(arcType));
    }
}
