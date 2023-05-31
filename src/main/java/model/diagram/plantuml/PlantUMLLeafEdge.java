package model.diagram.plantuml;

import model.graph.Arc;
import model.graph.ArcType;
import model.graph.SinkVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlantUMLLeafEdge {

	private final Map<Arc<SinkVertex>, Integer> graphEdges;
	private final List<String> plantUMLTester;

	public PlantUMLLeafEdge(Map<Arc<SinkVertex>, Integer> graphEdges) {
		this.graphEdges = graphEdges;
		plantUMLTester = new ArrayList<>();
	}

	public StringBuilder convertPlantEdge() {
		StringBuilder plantUMLBuffer = new StringBuilder();
		for (Arc<SinkVertex> relationship : graphEdges.keySet()) {
			String plantUMLRelationship = relationship.getSourceVertex().getName() + " " ;
			plantUMLRelationship += transformPlantUMLRelationship(relationship.getArcType()) + " ";
			plantUMLRelationship += relationship.getTargetVertex().getName();

			plantUMLBuffer.append(plantUMLRelationship).append("\n");
			plantUMLTester.add(plantUMLRelationship);
		}
		return plantUMLBuffer;
	}
    
    private String transformPlantUMLRelationship(ArcType relationshipType) {
		return switch (relationshipType) {
			case EXTENSION -> "--|>";
			case AGGREGATION -> "--o";
			case DEPENDENCY -> "..>";
			case IMPLEMENTATION -> "..|>";
			default -> "-->"; // ASSOCIATION
			//case SELFREFERENCE: // SELF-REFERENCING
			//	return "";		// A -- A , SAME CLASS WITH -- IN BETWEEN
			//case COMPOSITION:
			//	return "--*";
		};
    }

	public List<String> getPlantUMLTester() {
		return plantUMLTester;
	}

}
