package model;

public class LeafBranch {
	private LeafNode startingLeafNode;
	private LeafNode endingLeafNode;
	private String branchType;
	
	public LeafBranch (LeafNode startingLeafNode, LeafNode endingLeafNode, String branchType) {
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
