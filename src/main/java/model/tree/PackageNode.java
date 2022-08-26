package model.tree;

import java.util.HashMap;
import java.util.Map;

/**This class is responsible for the implementation of a package node in the tree.
 * Each has node has a parent node(the parent package), the path of the package folder,
 * the nodes children(the sub packages), the nodes leafs(the Java source files inside the
 * current package), a flag to identify if a package is empty or not
 */
public class PackageNode extends Node{
	private final Map<String, PackageNode> subNodes;
	private final Map<String, LeafNode> leafNodes;
	private boolean isValid;
	
	public PackageNode(String path) {
		super(path);
		this.isValid = false;
		subNodes = new HashMap<>();
		leafNodes = new HashMap<>();
	}
	
	public void addLeafNode(LeafNode leafNode) {
		leafNodes.put(leafNode.getName(), leafNode);
	}
	
	public void addSubNode(PackageNode packageNode) {
		subNodes.put(packageNode.getNodesPath(), packageNode);
	}

	public PackageNode getParentNode() {
		if (parentNode != null) {
			return parentNode;
		}else {
			return new PackageNode("");
		}
	}

	public void setValid() {
		this.isValid = true;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public Map<String, PackageNode> getSubNodes() {
		return subNodes;
	}

	public Map<String, LeafNode> getLeafNodes() {
		return leafNodes;
	}

	public String getName() {
		if (doesParentNodeExist()) {
			return getParentNodesName() + "." + path.substring(path.lastIndexOf("\\") + 1);
		}else {
			return path.substring(path.lastIndexOf("\\") + 1);
		}
	}

	private boolean doesParentNodeExist() {
		return !getParentNode().getNodesPath().isEmpty();
	}

	private String getParentNodesName() {
		return getParentNode().getName();
	}

	public NodeType getType() {
		return NodeType.PACKAGE;
	}

}
