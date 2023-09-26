package model.diagram;

import model.graph.Arc;
import model.graph.ArcType;
import model.graph.ClassifierVertex;

import java.util.*;

public class ShadowCleaner {

	private final List<ArcType> strongerToWeakerArcTypes;
	private final ClassDiagram classDiagram;

	public ShadowCleaner(ClassDiagram diagram) {
		classDiagram = diagram;
		strongerToWeakerArcTypes = List.of(ArcType.EXTENSION, ArcType.IMPLEMENTATION, ArcType.AGGREGATION, ArcType.ASSOCIATION);
	}

	public Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> shadowWeakRelationships() {
		for (Set<Arc<ClassifierVertex>> arcs: classDiagram.getDiagram().values()) {
			Map<ClassifierVertex, List<Arc<ClassifierVertex>>> shadowedArcs = new HashMap<>();
			for (Arc<ClassifierVertex> arc: arcs) {
				shadowedArcs.computeIfAbsent(arc.getTargetVertex(), sinkVertex -> new ArrayList<>()).add(arc);
			}

			for (Map.Entry<ClassifierVertex, List<Arc<ClassifierVertex>>> arc: shadowedArcs.entrySet()) {
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
		return classDiagram.getDiagram();
	}

	private boolean doWeakRelationshipsExist(Map.Entry<ClassifierVertex, List<Arc<ClassifierVertex>>> arc) {
		return arc.getValue().size() > 1;
	}

	private boolean doesStrongerRelationshipExist(List<Arc<ClassifierVertex>> arc, ArcType arcType) {
		Optional<Arc<ClassifierVertex>> inheritanceArc = arc.stream().filter(sinkVertexArc -> sinkVertexArc.getArcType().equals(arcType)).findFirst();
		return inheritanceArc.isPresent();
	}

	private void removeWeakerRelationships(Set<Arc<ClassifierVertex>> arcs, ClassifierVertex classifierVertex, ArcType arcType) {
		arcs.removeIf(arc -> arc.getTargetVertex().equals(classifierVertex) && !arc.getArcType().equals(arcType));
	}
}
