package model.diagram.plantuml;

import model.diagram.GraphEdgeCollection;
import model.tree.edge.Relationship;
import model.tree.edge.RelationshipType;

public class PlantUMLEdge extends GraphEdgeCollection{

	public String convertPlantEdge(Relationship relationship) {
		String plantUMLRelationship = relationship.getStartingNode().getName() + " " ;
		plantUMLRelationship += transformPlantUMLRelationship(relationship.getRelationshipType()) + " ";
		plantUMLRelationship += relationship.getEndingNode().getName();
		return plantUMLRelationship;
	}
    
    private String transformPlantUMLRelationship(RelationshipType relationshipType) {
    	switch (relationshipType) {
    		case EXTENSION:
    			return "--|>";
    		case AGGREGATION:
    			return "--o";
    		case DEPENDENCY:
    			return "..>";
    		case IMPLEMENTATION:
    			return "..|>";
    		default:			// ASSOCIATION
    			return "-->";
    		//case SELFREFERENCE: // SELF-REFERENCING
    		//	return "";		// A -- A , SAME CLASS WITH -- IN BETWEEN
        	//case COMPOSITION:
        	//	return "--*";
    	}
    }
    
	public String convertEdge(Relationship relationship, int edgeId) {
		// Do nothing
		return null;
	}

}
