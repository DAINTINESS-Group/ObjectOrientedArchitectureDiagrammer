package model;

import java.util.List;

public class LeafNodeRelation {
	private List<PackageNode> packageNodes;
	private List<LeafNode> allLeafNodes;
	
	public LeafNodeRelation (List<PackageNode> packageNodes) {
		this.packageNodes = packageNodes;
		iteratePackages();
	}
	
	private void iteratePackages() {
		for (PackageNode p: packageNodes) {
			for (LeafNode l: p.getLeafNodes()) {
				allLeafNodes.add(l);
			}
		}
		findLeafNodesRelations();
	}
	
	private void findLeafNodesRelations() {
		for (int i = 0; i < allLeafNodes.size(); i++) {
			  for (int j = i+1; j < allLeafNodes.size(); j++) {
				  if (branchExists(i, j)) {
					  createBranch(i, j);
				  }
				  if (branchExists(j, i)) {
					  createBranch(j, i);
				  }
			  }
		}
	}
	
	private boolean branchExists(int i, int j) {
		return (doesBranchExist(allLeafNodes.get(i).getMethodParameterTypes(), allLeafNodes.get(j).getName()) || 
				doesBranchExist(allLeafNodes.get(i).getFieldTypes(), allLeafNodes.get(j).getName()) ||
				doesBranchExist(allLeafNodes.get(i).getMethodReturnTypes(), allLeafNodes.get(j).getName())
				);
	}
	
	private boolean doesBranchExist(List<String> leafNodesTypes, String leafNodesName) {
		for (int i = 0; i < leafNodesTypes.size(); i++) {
			  if (leafNodesName.equals(leafNodesTypes.get(i))) {
				  return true;
			  }
		}
		return false;
	}
	
	private void createBranch(int i, int j) {
		LeafBranch leafBranch = new LeafBranch(allLeafNodes.get(i), allLeafNodes.get(j));
	}
	
}
