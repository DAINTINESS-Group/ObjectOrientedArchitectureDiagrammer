package model;

import java.util.ArrayList;
import java.util.List;

public class LeafNode {
	private String path;
	private PackageNode parentNode;
	private String name;
	private List<String> methodReturnTypes;
	private List<String> fieldTypes;
	private List<List<String>> methodParameterTypes;

	public LeafNode(String path) {
		methodReturnTypes = new ArrayList<String>();
		methodParameterTypes = new ArrayList<List<String>>();
		fieldTypes = new ArrayList<String>();
		this.path = path;
		setName();
	}
	
	public void setName() {
		this.name = path.substring(path.lastIndexOf("\\") + 1);
	}
	
	public void setParrentNode(PackageNode p) {
		this.parentNode = p;
	}
	
	public void addMethodParameterType(List<String> parameterTypes) {
		methodParameterTypes.add(parameterTypes);
	}
	
	public void addMethodReturnType(String returnType) {
		methodReturnTypes.add(returnType);
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
		return methodReturnTypes;
	}
	
	public List<String> getFieldTypes() {
		return fieldTypes;
	}
	
	public List<List<String>> getMethodParameterTypes() {
		return methodParameterTypes;
	}

}
