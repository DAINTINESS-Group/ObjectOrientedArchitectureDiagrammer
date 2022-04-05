package model;

public class LeafNode {
	private String path;
	private PackageNode parentNode;
	private String name;

	public LeafNode(String path) {
		this.path = path;
		setName();
	}
	
	public void setName() {
		this.name = path.substring(path.lastIndexOf("\\") + 1);
	}
	
	public void setParrentNode(PackageNode p) {
		this.parentNode = p;
	}
	
	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}
}
