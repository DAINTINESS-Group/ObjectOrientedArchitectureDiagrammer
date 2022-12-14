package model.tree;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**This class is responsible for the creation of the branches between the Java
 * source files. The branches have a type, e.g., inheritance, implementation.
 * The branches are also directed with a starting and an ending node
 */
public class RelationshipIdentifier {
	public static final int DECLARATION_LINE_STANDARD_SIZE = 2;
	public static final int INHERITANCE_TYPE = 2;
	public static final int SUPERCLASS_NAME = 3;
	public static final int MULTIPLE_IMPLEMENTATIONS = 5;
	public static final int INHERITANCE_TYPE_WITH_MULTIPLE_IMPLEMENTATIONS = 4;
	private final Map<Path, PackageNode> packageNodes;
	private final List<LeafNode> allLeafNodes;

	/**This method is responsible for retrieving the leaf nodes that have been created
	 * and then creating the branches between them
	 * @param packageNodes a collection with the package nodes created by the parser
	 */
	public RelationshipIdentifier(Map<Path, PackageNode> packageNodes) {
		this.packageNodes = packageNodes;
		allLeafNodes = new ArrayList<>();
		populateLeafNodes();
		identifyLeafNodesRelationship();
	}
	
	private void populateLeafNodes() {
		for (PackageNode p: packageNodes.values()) {
			allLeafNodes.addAll(p.getLeafNodes().values());
		}
	}
	
	private void identifyLeafNodesRelationship() {
		for (int i = 0; i < allLeafNodes.size(); i++) {
			  for (int j = i+1; j < allLeafNodes.size(); j++) {
				  checkRelationship(i, j);
				  checkRelationship(j, i);
			  }
		}
	}
	
	private void checkRelationship(int i, int j) {
		if (isDependency(i, j)) {
			createRelationship(i, j, RelationshipType.DEPENDENCY);
		}
		if (isAggregation(i, j)) {
			createRelationship(i, j, RelationshipType.AGGREGATION);
		}else if (isAssociation(i, j)) {
			createRelationship(i, j, RelationshipType.ASSOCIATION);
		}
		if (isInheritance(i)) {
			if (isExtension(i, j)) {
				createRelationship(i, j, RelationshipType.EXTENSION);
			}
			if (isImplementation(i, j)) {
				createRelationship(i, j, RelationshipType.IMPLEMENTATION);
			}
		}
	}
	
	private boolean isDependency(int i, int j) {
		return doesRelationshipExist(allLeafNodes.get(i).getMethodParameterTypes(), allLeafNodes.get(j).getName()) ||
				doesRelationshipExist(allLeafNodes.get(i).getMethodsReturnTypes(), allLeafNodes.get(j).getName());
	}

	private boolean isAssociation(int i, int j) {
		return doesRelationshipExist(allLeafNodes.get(i).getFieldsTypes(), allLeafNodes.get(j).getName());
	}
	
	private boolean isAggregation(int i, int j) {
		return isRelationshipAggregation(allLeafNodes.get(i).getFieldsTypes(), allLeafNodes.get(j).getName());
	}
	
	private boolean isInheritance(int i) {
		return allLeafNodes.get(i).getInheritanceLine().length > DECLARATION_LINE_STANDARD_SIZE;
	}
	
	private boolean isExtension(int i, int j) {
		if ( allLeafNodes.get(i).getInheritanceLine()[INHERITANCE_TYPE].equals("extends") ) {
			for (int k = 0; k < allLeafNodes.size(); k++) {
				if (allLeafNodes.get(i).getInheritanceLine()[SUPERCLASS_NAME].equals(allLeafNodes.get(j).getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isImplementation(int i, int j) {
		if ( allLeafNodes.get(i).getInheritanceLine()[INHERITANCE_TYPE].equals("implements") ) {
			for (int l = 3; l < allLeafNodes.get(i).getInheritanceLine().length; l++) {
				if (allLeafNodes.get(i).getInheritanceLine()[l].equals(allLeafNodes.get(j).getName())) {
					return true;
				}
			}
		}else if (allLeafNodes.get(i).getInheritanceLine().length > MULTIPLE_IMPLEMENTATIONS &&
				allLeafNodes.get(i).getInheritanceLine()[INHERITANCE_TYPE_WITH_MULTIPLE_IMPLEMENTATIONS].equals("implements")) {
			for (int l = 5; l < allLeafNodes.get(i).getInheritanceLine().length; l++) {
				if (allLeafNodes.get(i).getInheritanceLine()[l].equals(allLeafNodes.get(j).getName())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean doesRelationshipExist(List<String> leafNodesTypes, String leafNodesName) {
		for (String leafNodesType : leafNodesTypes) {
			if (doesFieldBelongToClass(leafNodesType, leafNodesName)) {
				return true;
			}
		}
		return false;
	}

	private boolean isRelationshipAggregation(List<String> leafNodesTypes, String leafNodesName) {
		for (String leafNodeType: leafNodesTypes) {
			if (isFieldOfTypeCollection(leafNodeType, leafNodesName) && doesFieldBelongToClass(leafNodeType, leafNodesName)){
				return true;
			}
		}
		return false;
	}
	
	private boolean doesFieldBelongToClass(String leafNodesType, String leafNodesName) {
		for (String type: leafNodesType.replace("[", ",").replace("]", ",").split(",")) {
			if (leafNodesName.equals(type)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isFieldOfTypeCollection(String s, String leafNodesName) {
		return (s.startsWith("List") || s.startsWith("ArrayList") || s.startsWith("Map") || s.startsWith("HashMap")
				|| s.contains(leafNodesName+"[") || s.startsWith("ArrayDeque") ||  s.startsWith("LinkedList") || s.startsWith("PriorityQueue"));
	}
	
	private void createRelationship(int i, int j, RelationshipType relationshipType) {
		allLeafNodes.get(i).addNodeRelationship(new Relationship(allLeafNodes.get(i), allLeafNodes.get(j), relationshipType));
		for (Relationship r: allLeafNodes.get(i).getParentNode().getNodeRelationships()) {
			if (doesPackageRelationshipAlreadyExist(j, r)) {
				return;
			}
		}
		if (!isRelationshipBetweenTheSamePackages(i, j)) {
			allLeafNodes.get(i).getParentNode().addNodeRelationship(new Relationship(allLeafNodes.get(i).getParentNode(),
					allLeafNodes.get(j).getParentNode(), RelationshipType.DEPENDENCY));
		}

	}

	private boolean doesPackageRelationshipAlreadyExist(int j, Relationship r) {
		return r.getEndingNode().equals(allLeafNodes.get(j).getParentNode());
	}

	private boolean isRelationshipBetweenTheSamePackages(int i, int j) {
		return allLeafNodes.get(i).getParentNode().equals(allLeafNodes.get(j).getParentNode());
	}

}
