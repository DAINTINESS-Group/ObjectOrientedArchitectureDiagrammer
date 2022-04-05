package model;

import java.util.ArrayList;
import java.util.List;

public class PackageNode {
	private String path;
	private PackageNode parentNode;
	private List<PackageNode> subNodes;
	private List<LeafNode> leafNodes;
	private boolean isValid;
	
	public PackageNode(String path) {
		this.path = path;
		this.isValid = false;
		subNodes = new ArrayList<PackageNode>();
		leafNodes = new ArrayList<LeafNode>();
	}
	
	public void addLeafNode(LeafNode leafNode) {
		leafNodes.add(leafNode);
	}
	
	public void addSubNode(PackageNode p) {
		subNodes.add(p);
	}
	
	public void setParentNode(PackageNode p) {
		this.parentNode = p;
	}

	public PackageNode getParentNode() {
		return parentNode;
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
	
	public List<PackageNode> getSubNodes() {
		return subNodes;
	}

	public List<LeafNode> getLeafNodes() {
		return leafNodes;
	}
	
	public String getName() {
		return path.substring(path.lastIndexOf("\\") + 1);
	}
}
