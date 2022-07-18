package model;

public class Relationship<T> {
	private final T startingLeafNode;
	private final T endingLeafNode;
	private final RelationshipType relationshipType;
	
	public Relationship(T startingLeafNode, T endingLeafNode, RelationshipType relationshipType) {
		this.startingLeafNode = startingLeafNode;
		this.endingLeafNode = endingLeafNode;
		this.relationshipType = relationshipType;
	}
	
	public RelationshipType getRelationshipType() {
		return relationshipType;
	}
	
	public T getEndingNode() {
		return endingLeafNode;
	}
	
	public T getStartingNode() {
		return startingLeafNode;
	}
}
