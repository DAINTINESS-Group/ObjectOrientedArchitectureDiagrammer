package model.tree;

public class Relationship {
	private final Node startingLeafNode;
	private final Node endingLeafNode;
	private final RelationshipType relationshipType;
	
	public Relationship(Node startingLeafNode, Node endingLeafNode, RelationshipType relationshipType) {
		this.startingLeafNode = startingLeafNode;
		this.endingLeafNode = endingLeafNode;
		this.relationshipType = relationshipType;
	}
	
	public RelationshipType getRelationshipType() {
		return relationshipType;
	}
	
	public Node getEndingNode() {
		return endingLeafNode;
	}
	
	public Node getStartingNode() {
		return startingLeafNode;
	}
}
