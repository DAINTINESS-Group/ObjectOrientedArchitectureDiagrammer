package model;

import java.util.ArrayList;
import java.util.List;

public class LeafNode {
	private String path;
	private PackageNode parentNode;
	private String name;
	private List<String> methodsReturnTypes;
	private List<String> fieldTypes;
	private List<String> methodsParameterTypes;

	public LeafNode(String path) {
		methodsReturnTypes = new ArrayList<String>();
		methodsParameterTypes = new ArrayList<String>();
		fieldTypes = new ArrayList<String>();
		this.path = path;
		setName();
	}
	
	private void setName() {
		this.name = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));
	}
	
	public void setParrentNode(PackageNode p) {
		this.parentNode = p;
	}
	
	public void addMethodParameterType(List<String> parameterTypes) {
		for (String s: parameterTypes) {
			methodsParameterTypes.add(s);
		}
	}
	
	public void addMethodReturnType(String returnType) {
		methodsReturnTypes.add(returnType);
	}
	
	public void addFieldType(String fieldType) {
		fieldTypes.add(fieldType);
	}
	
	public PackageNode getParentNode() {
		return parentNode;
	}
	
	public String getName() {
		return name;
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

}
