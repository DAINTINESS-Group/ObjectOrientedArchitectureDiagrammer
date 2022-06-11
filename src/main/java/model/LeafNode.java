package model;

import java.util.ArrayList;
import java.util.List;

/* This class is responsible for the implementation of a leaf node in the tree.
 * Each has node has a parent node(the parent package), the path of the source file,
 * the branches that start from that node and also the field/method/method parameter types */
public class LeafNode {
	private String path;
	private PackageNode parentNode;
	private String inheritanceLine[];
	private List<String> methodsReturnTypes;
	private List<String> fieldTypes;
	private List<String> methodsParameterTypes;
	private List<RelationshipBranch> leafBranches;
	
	/* This method is responsible for initializing the nodes structs */
	public LeafNode(String path) {
		methodsReturnTypes = new ArrayList<String>();
		methodsParameterTypes = new ArrayList<String>();
		fieldTypes = new ArrayList<String>();
		leafBranches = new ArrayList<RelationshipBranch>();
		this.path = path;
	}
	
	/* This method is responsible for setting the nodes line that contains the declaration
	 *  of the source file */
	public void setInheritanceLine(String[] inheritanceLine) {
		this.inheritanceLine = inheritanceLine;
	}
	
	/* This method is responsible for setting the nodes parent node, the parent package */
	public void setParrentNode(PackageNode p) {
		this.parentNode = p;
	}
	
	/* This method is responsible for adding to the nodes method parameter types */
	public void addMethodParameterType(List<String> parameterTypes) {
		for (String s: parameterTypes) {
			methodsParameterTypes.add(s);
		}
	}
	
	/* This method is responsible for adding a leaf branch that starts from the 
	 * current node */
	public void addLeafBranch(RelationshipBranch l) {
		leafBranches.add(l);
	}
	
	/* This method is responsible for adding to the nodes method return types */
	public void addMethodReturnType(String returnType) {
		methodsReturnTypes.add(returnType);
	}
	
	/* This method is responsible for adding to the nodes field types */
	public void addFieldType(String fieldType) {
		fieldTypes.add(fieldType);
	}
	
	public PackageNode getParentNode() {
		return parentNode;
	}
	
	public String[] getInheritanceLine() {
		return inheritanceLine;
	}
	
	public String getName() {
		return path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));
	}

	public String getPath() {
		return path;
	}
	
	public List<String> getMethodReturnTypes() {
		return methodsReturnTypes;
	}
	
	public List<String> getFieldTypes() {
		return fieldTypes;
	}
	
	public List<String> getMethodParameterTypes() {
		return methodsParameterTypes;
	}

	public List<RelationshipBranch> getLeafBranches() {
		return leafBranches;
	}

	public String getType() {
		if (inheritanceLine[0].equals("enum")) {
			return "enum";
		}else if (inheritanceLine[0].equals("interface")) {
			return "interface";
		}else {
			return "class";
		}
	}

}
