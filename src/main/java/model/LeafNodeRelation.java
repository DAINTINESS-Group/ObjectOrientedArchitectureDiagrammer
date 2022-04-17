package model;

import java.util.ArrayList;
import java.util.List;

public class LeafNodeRelation {
	private List<PackageNode> packageNodes;
	private List<LeafNode> allLeafNodes;
	
	public LeafNodeRelation (List<PackageNode> packageNodes) {
		this.packageNodes = packageNodes;
		allLeafNodes = new ArrayList<LeafNode>();
		populateLeafNodes();
		findLeafNodesRelations();
	}
	
	private void populateLeafNodes() {
		for (PackageNode p: packageNodes) {
			for (LeafNode l: p.getLeafNodes()) {
				allLeafNodes.add(l);
			}
		}
	}
	
	private void findLeafNodesRelations() {
		for (int i = 0; i < allLeafNodes.size(); i++) {
			  for (int j = i+1; j < allLeafNodes.size(); j++) {
				  checkRelation(i, j);
				  checkRelation(j, i);
			  }
		}
	}
	
	private void checkRelation(int i, int j) {
		if (doesBranchExist(allLeafNodes.get(i).getMethodParameterTypes(), allLeafNodes.get(j).getName()) ||
				doesBranchExist(allLeafNodes.get(i).getMethodReturnTypes(), allLeafNodes.get(j).getName())) {
			createBranch(i, j, "dependency");
		}else if (doesBranchExist(allLeafNodes.get(i).getFieldTypes(), allLeafNodes.get(j).getName())) {
			createBranch(i, j, "association");
		}
	}
	
	private boolean doesBranchExist(List<String> leafNodesTypes, String leafNodesName) {
		for (int i = 0; i < leafNodesTypes.size(); i++) {
			  if (leafNodesName.equals(leafNodesTypes.get(i))) {
				  return true;
			  }
		}
		return false;
	}
	
	private void createBranch(int i, int j, String branchType) {
		allLeafNodes.get(i).addLeafBranch(new LeafBranch(allLeafNodes.get(i), allLeafNodes.get(j), branchType));
	}

}
