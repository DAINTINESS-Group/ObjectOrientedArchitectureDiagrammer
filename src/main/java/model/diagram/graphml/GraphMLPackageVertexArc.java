package model.diagram.graphml;

import model.diagram.PackageDiagram;
import model.graph.Arc;
import model.graph.PackageVertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GraphMLPackageVertexArc {
	private final StringBuilder  graphMLBuffer;
	private final PackageDiagram packageDiagram;

	public GraphMLPackageVertexArc(PackageDiagram packageDiagram) {
		this.packageDiagram = packageDiagram;
		graphMLBuffer 	    = new StringBuilder();
	}

	public StringBuilder convertVertexArc() {
		List<Arc<PackageVertex>> arcs = new ArrayList<>();
		for (Set<Arc<PackageVertex>> arcSet: packageDiagram.getDiagram().values()) {
			arcs.addAll(arcSet);
		}

		int edgeId = 0;
		for (Arc<PackageVertex> arc: arcs) {
			graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLVertexArcSyntax(getVertexArcProperties(arc, edgeId)));
			edgeId++;
		}
		return graphMLBuffer;
	}

	private List<String> getVertexArcProperties(Arc<PackageVertex> relationship, Integer edgeId) {
		return Arrays.asList(
			String.valueOf(edgeId),
			String.valueOf(packageDiagram.getGraphNodes().get(relationship.sourceVertex())),
			String.valueOf(packageDiagram.getGraphNodes().get(relationship.targetVertex()))
		);
	}

}
