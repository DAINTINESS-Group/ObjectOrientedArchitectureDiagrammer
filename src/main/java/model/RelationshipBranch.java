package model;

public class RelationshipBranch {
	private final LeafNode startingLeafNode;
	private final LeafNode endingLeafNode;
	private final String branchType;
	
	public RelationshipBranch (LeafNode startingLeafNode, LeafNode endingLeafNode, String branchType) {
		this.startingLeafNode = startingLeafNode;
		this.endingLeafNode = endingLeafNode;
		this.branchType = branchType;
	}
	
	public String getBranchType() {
		return branchType;
	}
	
	public LeafNode getEndingLeafNode() {
		return endingLeafNode;
	}
	
	public LeafNode getStartingLeafNode() {
		return startingLeafNode;
	}
}
