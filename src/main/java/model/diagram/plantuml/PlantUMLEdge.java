package model.diagram.plantuml;

import model.tree.edge.Relationship;
import model.tree.edge.RelationshipType;

public class PlantUMLEdge {

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
    
}
