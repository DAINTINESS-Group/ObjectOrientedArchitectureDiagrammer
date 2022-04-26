package model;

import java.util.ArrayList;
import java.util.Arrays;
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
		if (isDependency(i, j)) {
			createBranch(i, j, "dependency");
		}else if (isAggregation(i, j)) {
			createBranch(i, j, "aggregation");
		}else if (isAssociation(i, j)) {
			createBranch(i, j, "association");
		}
		if (isInheritance(i)) {
			if (isExtension(i, j)) {
				createBranch(i, j, "extension");
			}
			if (isImplementation(i, j)) {
				createBranch(i, j, "implementation");
			}
		}
	}
	
	private boolean isDependency(int i, int j) {
		return doesDependencyBranchExist(allLeafNodes.get(i).getMethodParameterTypes(), allLeafNodes.get(j).getName()) ||
				doesDependencyBranchExist(allLeafNodes.get(i).getMethodReturnTypes(), allLeafNodes.get(j).getName());
	}

	private boolean isAssociation(int i, int j) {
		return doesAssociationBranchExist(allLeafNodes.get(i).getFieldTypes(), allLeafNodes.get(j).getName());
	}
	
	private boolean isAggregation(int i, int j) {
		return doesAggregationBranchExist(allLeafNodes.get(i).getFieldTypes(), allLeafNodes.get(j).getName());
	}
	
	private boolean isInheritance(int i) {
		return allLeafNodes.get(i).getInheritanceLine().length > 0;
	}
	
	private boolean isExtension(int i, int j) {
		if ( allLeafNodes.get(i).getInheritanceLine()[0].equals("extends") ) {
			for (int k = 0; k < allLeafNodes.size(); k++) {
				if (allLeafNodes.get(i).getInheritanceLine()[1].equals(allLeafNodes.get(j).getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isImplementation(int i, int j) {
		if ( allLeafNodes.get(i).getInheritanceLine()[0].equals("implements") ) {
			for (int l = 1; l < allLeafNodes.get(i).getInheritanceLine().length-1; l++) {
				if (allLeafNodes.get(i).getInheritanceLine()[l].equals(allLeafNodes.get(j).getName())) {
					return true;
				}
			}
		}else if (allLeafNodes.get(i).getInheritanceLine().length > 3 && allLeafNodes.get(i).getInheritanceLine()[2].equals("implements")) {
			for (int l = 3; l < allLeafNodes.get(i).getInheritanceLine().length; l++) {
				if (allLeafNodes.get(i).getInheritanceLine()[l].equals(allLeafNodes.get(j).getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean doesDependencyBranchExist(List<String> leafNodesTypes, String leafNodesName) {
		for (int i = 0; i < leafNodesTypes.size(); i++) {
			if (leafNodesTypes.get(i).equals(leafNodesName)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean doesAssociationBranchExist(List<String> leafNodesTypes, String leafNodesName) {
		for (int i = 0; i < leafNodesTypes.size(); i++) {
			if (isFieldOfTypeClassObject(leafNodesTypes.get(i), leafNodesName)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean doesAggregationBranchExist(List<String> leafNodesTypes, String leafNodesName) {
		for (String s: leafNodesTypes) {
			if (isFieldOfTypeList(s, leafNodesName) && isListOfTypeClassObject(s, leafNodesName)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isFieldOfTypeClassObject(String leafNodesType, String leafNodesName) {
		return leafNodesName.equals(leafNodesType);
	}
	
	private boolean isListOfTypeClassObject(String fieldType, String leafNodesName) {
		if (leafNodesName.contains(fieldType)) {
			return true;
		}
		return false;
	}

	private boolean isFieldOfTypeList(String s, String leafNodesName) {
		return (s.startsWith("List") || s.startsWith("ArrayList") || s.startsWith("Map") || s.startsWith("HashMap") || s.contains(leafNodesName+"["));
	}
	
	private void createBranch(int i, int j, String branchType) {
		allLeafNodes.get(i).addLeafBranch(new LeafBranch(allLeafNodes.get(i), allLeafNodes.get(j), branchType));
	}

}
