package model;

public class LeafBranch {
	private LeafNode startingLeafNode;
	private LeafNode endingLeafNode;
	private String branchType;
	
	public LeafBranch (LeafNode startingLeafNode, LeafNode endingLeafNode) {
		this.startingLeafNode = startingLeafNode;
		this.endingLeafNode = endingLeafNode;
	}
	
	public void setBranchType() {
		
	}
	
	public LeafNode getEndingLeafNode() {
		return endingLeafNode;
	}
	
	public LeafNode getStartingLeafNode() {
		return startingLeafNode;
	}
}
