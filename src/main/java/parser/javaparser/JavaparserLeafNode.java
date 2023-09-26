package parser.javaparser;

import parser.tree.LeafNode;
import parser.tree.NodeType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaparserLeafNode extends LeafNode {

	private final List<String> implementedInterfaces;
	private final Map<String, String> variables;
	private final List<String> createdObjects;
	private String nodeName;
	private String baseClass;
	private NodeType nodeType;
	private List<String> imports;

	public JavaparserLeafNode(Path path) {
		super(path);
		implementedInterfaces = new ArrayList<>();
		variables = new HashMap<>();
		createdObjects = new ArrayList<>();
		baseClass = "";
		nodeName = "";
	}

	public void addVariable(String variableName, String variableType) {
		variables.put(variableName, variableType);
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
		implementedInterfaces.add(interfaceName);
	}

	public void addCreatedObject(String createdObject) {
		createdObjects.add(createdObject);
	}

	public void setImports(List<String> imports) {
		this.imports = imports;
	}

	public List<String> getVariablesTypes() {
		return new ArrayList<>(variables.values());
	}

	public String getBaseClass() {
		return baseClass;
	}

	public List<String> getCreatedObjects() {
		return createdObjects;
	}

	public List<String> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public List<String> getImports() {
		return imports;
	}

	@Override
	public String getName() {
		return nodeName;
	}

	@Override
	public NodeType getType() {
		return nodeType;
	}
}