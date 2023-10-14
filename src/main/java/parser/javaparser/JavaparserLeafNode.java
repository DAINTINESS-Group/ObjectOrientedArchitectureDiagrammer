package parser.javaparser;

import parser.tree.LeafNode;
import parser.tree.NodeType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaparserLeafNode extends LeafNode {

	private final List<String> 		  implementedInterfaces;
	private final Map<String, String> variables;
	private final List<String> 		  createdObjects;
	private 	  String 			  nodeName;
	private 	  String 			  baseClass;
	private 	  NodeType 			  nodeType;
	private 	  List<String> 		  imports;

	public JavaparserLeafNode(Path path) {
		super(path);
		this.implementedInterfaces = new ArrayList<>();
		this.variables 			   = new HashMap<>();
		this.createdObjects 	   = new ArrayList<>();
		this.baseClass 			   = "";
		this.nodeName 			   = "";
	}

	public void addVariable(String variableName, String variableType) {
		this.variables.put(variableName, variableType);
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public void setBaseClass(String baseClass) {
		this.baseClass = baseClass;
	}

	public void addImplementedInterface(String interfaceName) {
		this.implementedInterfaces.add(interfaceName);
	}

	public void addCreatedObject(String createdObject) {
		this.createdObjects.add(createdObject);
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
	}

	public List<String> getVariablesTypes() {
		return new ArrayList<>(this.variables.values());
	}

	public String getBaseClass() {
		return this.baseClass;
	}

	public List<String> getCreatedObjects() {
		return this.createdObjects;
	}

	public List<String> getImplementedInterfaces() {
		return this.implementedInterfaces;
	}

	public List<String> getImports() {
		return this.imports;
	}

	@Override
	public String getName() {
		return this.nodeName;
	}

	@Override
	public NodeType getType() {
		return this.nodeType;
	}
}