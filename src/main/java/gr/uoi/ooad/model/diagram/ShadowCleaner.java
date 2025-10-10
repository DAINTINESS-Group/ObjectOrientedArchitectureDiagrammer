package gr.uoi.ooad.model.diagram;

import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import java.util.*;

public class ShadowCleaner {

    // TODO Replace with the new ArcType Map.
    private static final List<ArcType> strongerToWeakerArcTypes =
            List.of(
                    ArcType.EXTENSION,
                    ArcType.IMPLEMENTATION,
                    ArcType.AGGREGATION,
                    ArcType.ASSOCIATION);

    private final ClassDiagram classDiagram;

    public ShadowCleaner(ClassDiagram diagram) {
        this.classDiagram = diagram;
    }

    public Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> shadowWeakRelationships() {
        for (Set<Arc<ClassifierVertex>> arcs : classDiagram.getDiagram().values()) {
            Map<ClassifierVertex, List<Arc<ClassifierVertex>>> shadowedArcs = new HashMap<>();
            for (Arc<ClassifierVertex> arc : arcs) {
                shadowedArcs.computeIfAbsent(arc.targetVertex(), k -> new ArrayList<>()).add(arc);
            }

            for (Map.Entry<ClassifierVertex, List<Arc<ClassifierVertex>>> arc :
                    shadowedArcs.entrySet()) {
                if (!(arc.getValue().size() > 1)) continue;

                for (ArcType arcType : strongerToWeakerArcTypes) {
                    if (!doesStrongerRelationshipExist(arc.getValue(), arcType)) continue;

                    removeWeakerRelationships(arcs, arc.getKey(), arcType);
                    break;
                }
            }
        }

        return classDiagram.getDiagram();
    }

    private boolean doesStrongerRelationshipExist(
            List<Arc<ClassifierVertex>> arc, ArcType arcType) {
        return arc.stream().anyMatch(it -> it.arcType().equals(arcType));
    }

    private void removeWeakerRelationships(
            Set<Arc<ClassifierVertex>> arcs, ClassifierVertex classifierVertex, ArcType arcType) {
        arcs.removeIf(
                it -> it.targetVertex().equals(classifierVertex) && !it.arcType().equals(arcType));
    }
}
