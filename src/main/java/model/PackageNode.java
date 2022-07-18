package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* This class is responsible for the implementation of a package node in the tree.
 * Each has node has a parent node(the parent package), the path of the package folder,
 * the nodes children(the sub packages), the nodes leafs(the Java source files inside the
 * current package), a flag to identify if a package is empty or not  */
public class PackageNode {
	private final String path;
	private PackageNode parentNode;
	private final Map<String, PackageNode> subNodes;
	private final Map<String, LeafNode> leafNodes;

	private final List<Relationship<?>> packageNodeRelationships;
	private boolean isValid;
	
	public PackageNode(String path) {
		this.path = path;
		this.isValid = false;
		subNodes = new HashMap<>();
		leafNodes = new HashMap<>();
		packageNodeRelationships = new ArrayList<>();
	}
	
	public void addLeafNode(LeafNode leafNode) {
		leafNodes.put(leafNode.getName(), leafNode);
	}
	
	public void addSubNode(PackageNode p) {
		subNodes.put(p.getName(), p);
	}

	public void addPackageNodeRelationship(Relationship<?> r) {
		packageNodeRelationships.add(r);
	}
	
	public void setParentNode(PackageNode p) {
		this.parentNode = p;
	}

	public PackageNode getParentNode() {
		if (parentNode != null) {
			return parentNode;
		}else {
			return new PackageNode("");
		}
	}
	
	public String getNodesPath() {
		return path;
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
		return path.substring(path.lastIndexOf("\\") + 1);
	}

	public List<Relationship<?>> getPackageNodeRelationships() {
		return packageNodeRelationships;
	}
}
