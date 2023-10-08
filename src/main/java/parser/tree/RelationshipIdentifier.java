package parser.tree;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for the creation of the branches between the Java
 * source files. The branches have a type, e.g., inheritance, implementation.
 * The branches are also directed with a starting and an ending node
 */
public abstract class RelationshipIdentifier {
	private final Map<Path, PackageNode> packageNodes;
	protected final List<LeafNode> allLeafNodes;
	private int relationshipsCreated;

	/**
	 * This method is responsible for retrieving the leaf nodes that have been created
	 * and then creating the branches between them
	 * @param packageNodes a collection with the package nodes created by the parser
	 */
	public RelationshipIdentifier(Map<Path, PackageNode> packageNodes) {
		this.packageNodes = packageNodes;
		allLeafNodes = new ArrayList<>();
		relationshipsCreated = 0;
		populateLeafNodes();
	}

	public int createLeafNodesRelationships() {
		for (int i = 0; i < allLeafNodes.size(); i++) {
			for (int j = i+1; j < allLeafNodes.size(); j++) {
				checkRelationship(i, j);
				checkRelationship(j, i);
			}
		}
		return relationshipsCreated;
	}

	private void populateLeafNodes() {
		for (PackageNode p: packageNodes.values()) {
			allLeafNodes.addAll(p.getLeafNodes().values());
		}
	}

	protected boolean isAssociation(int i, int j) {
		return doesRelationshipExist(allLeafNodes.get(i).getFieldsTypes(), allLeafNodes.get(j).getName());
	}

	protected boolean isAggregation(int i, int j) {
		return isRelationshipAggregation(allLeafNodes.get(i).getFieldsTypes(), allLeafNodes.get(j).getName());
	}

	protected boolean doesRelationshipExist(List<String> leafNodesTypes, String leafNodesName) {
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

	protected void createRelationship(int i, int j, RelationshipType relationshipType) {
		allLeafNodes.get(i).addLeafNodeRelationship(new Relationship<>(allLeafNodes.get(i), allLeafNodes.get(j), relationshipType));
		relationshipsCreated++;
		for (Relationship<PackageNode> r: allLeafNodes.get(i).getParentNode().getPackageNodeRelationships()) {
			if (doesPackageRelationshipAlreadyExist(j, r)) {
				return;
			}
		}
		if (isRelationshipBetweenTheSamePackages(i, j)) {
			return;
		}
		allLeafNodes.get(i).getParentNode().addPackageNodeRelationship(new Relationship<>(allLeafNodes.get(i).getParentNode(),
				allLeafNodes.get(j).getParentNode(), RelationshipType.DEPENDENCY));
		relationshipsCreated++;
	}

	private boolean doesPackageRelationshipAlreadyExist(int j, Relationship<PackageNode> r) {
		return r.getEndingNode().equals(allLeafNodes.get(j).getParentNode());
	}

	private boolean isRelationshipBetweenTheSamePackages(int i, int j) {
		return allLeafNodes.get(i).getParentNode().equals(allLeafNodes.get(j).getParentNode());
	}

	protected abstract void checkRelationship(int i, int j);

	protected abstract boolean isDependency(int i, int j);

	protected abstract boolean isExtension(int i, int j);

	protected abstract boolean isImplementation(int i, int j);

}
