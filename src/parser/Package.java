package parser;

import java.util.ArrayList;
import java.util.List;

public class Package {
	private String path;
	private Package parent;
	private List<Package> children;
	private List<String> leafs;
	private boolean isValid;
	
	public Package(String path) {
		this.path = path;
		this.isValid = false;
		children = new ArrayList<Package>();
		leafs = new ArrayList<String>();
	}
	
	public void addLeafNode(String leafsPath) {
		leafs.add(leafsPath);
	}
	
	public void addChild(Package p) {
		children.add(p);
	}
	
	public void setParent(Package p) {
		this.parent = p;
	}

	public Package getParent() {
		return parent;
	}
	
	public String getPath() {
		return path;
	}
	
	public void printChildren() {
		for (Package p: children) {
			System.out.println(p.getPath());
		}
	}
	
	public void printLeafs() {
		for (String l: leafs) {
			System.out.println(l);
		}
	}

	public void setValid() {
		this.isValid = true;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public String getName() {
		return path.substring(path.lastIndexOf("\\") + 1);
	}
	
	public String toString() {
		return this.path;
	}
}
