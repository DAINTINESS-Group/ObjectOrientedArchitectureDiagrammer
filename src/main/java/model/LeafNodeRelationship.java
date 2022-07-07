package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/* This class is responsible for the creation of the branches between the Java 
 * source files. The branches have a type, e.g., inheritance, implementation.
 * The branches are also directed with a starting and an ending node*/
public class LeafNodeRelationship {
	private final Map<String, PackageNode> packageNodes;
	private final List<LeafNode> allLeafNodes;
	
	/* This method is responsible for retrieving the leaf nodes that have been created
	 * and then creating the branches between them. */
	public LeafNodeRelationship (Map<String, PackageNode> packageNodes) {
		this.packageNodes = packageNodes;
		allLeafNodes = new ArrayList<>();
		populateLeafNodes();
		findLeafNodesRelations();
	}
	
	private void populateLeafNodes() {
		for (PackageNode p: packageNodes.values()) {
			allLeafNodes.addAll(p.getLeafNodes().values());
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
				doesDependencyBranchExist(allLeafNodes.get(i).getMethodsReturnTypes(), allLeafNodes.get(j).getName());
	}

	private boolean isAssociation(int i, int j) {
		return doesAssociationBranchExist(allLeafNodes.get(i).getFieldsTypes(), allLeafNodes.get(j).getName());
	}
	
	private boolean isAggregation(int i, int j) {
		return doesAggregationBranchExist(allLeafNodes.get(i).getFieldsTypes(), allLeafNodes.get(j).getName());
	}
	
	private boolean isInheritance(int i) {
		return allLeafNodes.get(i).getInheritanceLine().length > 2;
	}
	
	private boolean isExtension(int i, int j) {
		if ( allLeafNodes.get(i).getInheritanceLine()[2].equals("extends") ) {
			for (int k = 0; k < allLeafNodes.size(); k++) {
				if (allLeafNodes.get(i).getInheritanceLine()[3].equals(allLeafNodes.get(j).getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isImplementation(int i, int j) {
		if ( allLeafNodes.get(i).getInheritanceLine()[2].equals("implements") ) {
			for (int l = 3; l < allLeafNodes.get(i).getInheritanceLine().length; l++) {
				if (allLeafNodes.get(i).getInheritanceLine()[l].equals(allLeafNodes.get(j).getName())) {
					return true;
				}
			}
		}else if (allLeafNodes.get(i).getInheritanceLine().length > 5 && allLeafNodes.get(i).getInheritanceLine()[4].equals("implements")) {
			for (int l = 5; l < allLeafNodes.get(i).getInheritanceLine().length; l++) {
				if (allLeafNodes.get(i).getInheritanceLine()[l].equals(allLeafNodes.get(j).getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean doesDependencyBranchExist(List<String> leafNodesTypes, String leafNodesName) {
		for (String leafNodesType : leafNodesTypes) {
			if (leafNodesType.equals(leafNodesName)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean doesAssociationBranchExist(List<String> leafNodesTypes, String leafNodesName) {
		for (String leafNodesType : leafNodesTypes) {
			if (isFieldOfTypeClassObject(leafNodesType, leafNodesName)) {
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
		return leafNodesName.contains(fieldType);
	}

	private boolean isFieldOfTypeList(String s, String leafNodesName) {
		return (s.startsWith("List") || s.startsWith("ArrayList") || s.startsWith("Map") || s.startsWith("HashMap") || s.contains(leafNodesName+"["));
	}
	
	private void createBranch(int i, int j, String branchType) {
		allLeafNodes.get(i).addLeafBranch(new RelationshipBranch(allLeafNodes.get(i), allLeafNodes.get(j), branchType));
	}

}
