package parser.tree;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for the implementation of a package node in the tree.
 * Each has node has a parent node(the parent package), the path of the package folder,
 * the nodes children(the sub packages), the nodes leafs(the Java source files inside the
 * current package), a flag to identify if a package is empty or not
 */
public class PackageNode {
	private final Map<Path, PackageNode> subNodes;
	private final Map<String, LeafNode> leafNodes;
	private final Path path;
	private PackageNode parentNode;
	private final List<Relationship<PackageNode>> packageNodeRelationships;
	private boolean isValid;

	public PackageNode(Path path) {
		this.path = path;
		packageNodeRelationships = new ArrayList<>();
		this.isValid = false;
		subNodes = new HashMap<>();
		leafNodes = new HashMap<>();
	}

	public void addLeafNode(LeafNode leafNode) {
		leafNodes.put(leafNode.getName(), leafNode);
	}

	public void addSubNode(PackageNode packageNode) {
		subNodes.put(packageNode.getPackageNodesPath(), packageNode);
	}

	public void setValid() {
		this.isValid = true;
	}

	public void setParentNode(PackageNode p) {
		this.parentNode = p;
	}

	public void addPackageNodeRelationship(Relationship<PackageNode> relationship) {
		packageNodeRelationships.add(relationship);
	}

	public boolean isValid() {
		return isValid;
	}

	public List<Relationship<PackageNode>> getPackageNodeRelationships() {
		return packageNodeRelationships;
	}

	public Path getPackageNodesPath() {
		return path;
	}

	public PackageNode getParentNode() {
		if (parentNode != null) {
			return parentNode;
		}else {
			return new PackageNode(Paths.get(""));
		}
	}

	public Map<Path, PackageNode> getSubNodes() {
		return subNodes;
	}

	public Map<String, LeafNode> getLeafNodes() {
		return leafNodes;
	}

	public String getName() {
		if (doesParentNodeExist()) {
			return getParentNodesName() + "." + path.getFileName().toString();
		}
		return path.getFileName().toString();
	}

	private boolean doesParentNodeExist() {
		return !getParentNode().getPackageNodesPath().normalize().toString().isEmpty();
	}

	private String getParentNodesName() {
		return getParentNode().getName();
	}

	public NodeType getType() {
		return NodeType.PACKAGE;
	}

}
