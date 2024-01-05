package model.diagram;

import model.graph.Arc;
import model.graph.ArcType;
import model.graph.ClassifierVertex;

import java.util.*;

public class ShadowCleaner {

	// TODO Replace with the new ArcType Map
	private static final List<ArcType> strongerToWeakerArcTypes = List.of(ArcType.EXTENSION,
																		  ArcType.IMPLEMENTATION,
																		  ArcType.AGGREGATION,
																		  ArcType.ASSOCIATION);

	private final 		 ClassDiagram  classDiagram;

	public ShadowCleaner(ClassDiagram diagram) {
		this.classDiagram = diagram;
	}

	public Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> shadowWeakRelationships() {
		for (Set<Arc<ClassifierVertex>> arcs: classDiagram.getDiagram().values()) {
			Map<ClassifierVertex, List<Arc<ClassifierVertex>>> shadowedArcs = new HashMap<>();
			for (Arc<ClassifierVertex> arc: arcs) {
				shadowedArcs.computeIfAbsent(arc.targetVertex(),
											 sinkVertex -> new ArrayList<>()).add(arc);
			}

			for (Map.Entry<ClassifierVertex, List<Arc<ClassifierVertex>>> arc: shadowedArcs.entrySet()) {
				if (!(arc.getValue().size() > 1)) {
					continue;
				}
				for (ArcType arcType: strongerToWeakerArcTypes) {
                    if (!doesStrongerRelationshipExist(arc.getValue(), arcType)) {
						continue;
					}
                    removeWeakerRelationships(arcs, arc.getKey(), arcType);
                    break;
                }
			}
		}

		return classDiagram.getDiagram();
	}

	private boolean doesStrongerRelationshipExist(List<Arc<ClassifierVertex>> arc,
												  ArcType 					  arcType) {
		Optional<Arc<ClassifierVertex>> inheritanceArc = arc
			.stream()
			.filter(sinkVertexArc -> sinkVertexArc.arcType().equals(arcType))
			.findFirst();
		return inheritanceArc.isPresent();
	}

	private void removeWeakerRelationships(Set<Arc<ClassifierVertex>> arcs,
										   ClassifierVertex 		  classifierVertex,
										   ArcType 					  arcType) {
		arcs.removeIf(arc ->
						  arc.targetVertex().equals(classifierVertex) &&
						  !arc.arcType().equals(arcType));
	}
}
