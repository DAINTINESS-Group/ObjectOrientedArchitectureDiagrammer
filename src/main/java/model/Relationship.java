package model;

public class Relationship {
	private final LeafNode startingLeafNode;
	private final LeafNode endingLeafNode;
	private final RelationshipType relationshipType;
	
	public Relationship(LeafNode startingLeafNode, LeafNode endingLeafNode, RelationshipType relationshipType) {
		this.startingLeafNode = startingLeafNode;
		this.endingLeafNode = endingLeafNode;
		this.relationshipType = relationshipType;
	}
	
	public RelationshipType getRelationshipType() {
		return relationshipType;
	}
	
	public LeafNode getEndingLeafNode() {
		return endingLeafNode;
	}
	
	public LeafNode getStartingLeafNode() {
		return startingLeafNode;
	}
}
