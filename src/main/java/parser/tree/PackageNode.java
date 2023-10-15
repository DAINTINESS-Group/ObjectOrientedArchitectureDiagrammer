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
 * current package), a flag to identify if a package is empty or not.
 */
public class PackageNode {
	private final Map<Path, PackageNode>		  subNodes;
	private final Map<String, LeafNode> 		  leafNodes;
	private final Path 							  path;
	private final List<Relationship<PackageNode>> packageNodeRelationships;
	private 	  PackageNode 					  parentNode;
	private 	  boolean 						  isValid;

	public PackageNode(Path path) {
		this.packageNodeRelationships = new ArrayList<>();
		this.subNodes 				  = new HashMap<>();
		this.leafNodes 				  = new HashMap<>();
		this.path 					  = path;
		this.isValid 				  = false;
	}

	public void addLeafNode(LeafNode leafNode) {
		this.leafNodes.put(leafNode.getName(), leafNode);
	}

	public void addSubNode(PackageNode packageNode) {
		this.subNodes.put(packageNode.getPackageNodesPath(), packageNode);
	}

	public void setValid() {
		this.isValid = true;
	}

	public void setParentNode(PackageNode p) {
		this.parentNode = p;
	}

	public void addPackageNodeRelationship(Relationship<PackageNode> relationship) {
		this.packageNodeRelationships.add(relationship);
	}

	public boolean isValid() {
		return this.isValid;
	}

	public List<Relationship<PackageNode>> getPackageNodeRelationships() {
		return this.packageNodeRelationships;
	}

	public Path getPackageNodesPath() {
		return this.path;
	}

	public PackageNode getParentNode() {
		if (this.parentNode != null) {
			return this.parentNode;
		}else {
			return new PackageNode(Paths.get(""));
		}
	}

	public Map<Path, PackageNode> getSubNodes() {
		return this.subNodes;
	}

	public Map<String, LeafNode> getLeafNodes() {
		return this.leafNodes;
	}

	public String getName() {
		if (doesParentNodeExist()) {
			return String.join(".", getParentNodesName(), this.path.getFileName().toString());
		}
		return this.path.getFileName().toString();
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
