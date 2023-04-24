package model.tree;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**This class is responsible for the implementation of a leaf node in the tree.
 * Each node has a parent node(the parent package), the path of the source file,
 * the branches that start from that node and also the field/method/method parameter types
 */
public class LeafNode extends Node{

	private NodeType nodeType;
	private String nodeName;
	private String baseClass;
	private final List<String> implementedInterfaces;
	private final Map<String, String> fields;
	private final Map<String, String> methods;
	private final Map<String, String> variables;
	private final List<String> methodsParametersTypes;
	private final List<String> createdObjects;

	/**The constructor is responsible for initializing the nodes structs
	 * @param path the path of Java source file
	 */
	public LeafNode(Path path) {
		super(path);
		fields = new HashMap<>();
		methods = new HashMap<>();
		variables = new HashMap<>();
		implementedInterfaces = new ArrayList<>();
		methodsParametersTypes = new ArrayList<>();
		createdObjects = new ArrayList<>();
		baseClass = "";
		nodeName = "";
	}

	/**This method is responsible for adding to the nodes' method parameter types
	 * @param methodParameterType the different types of parameters the Java source file's methods take
	 */
	public void addMethodParametersType(String methodParameterType) {
		methodsParametersTypes.add(methodParameterType);
	}

	/**This method is responsible for adding the nodes' methods names and return types
	 * @param methodName the method's name
	 * @param methodReturnType the method's return type
	 */
	public void addMethod(String methodName, String methodReturnType) {
		methods.put(methodName, methodReturnType);
	}

	/**This method is responsible for adding the nodes' fields names types
	 * @param fieldName the field's name
	 * @param fieldType the field's type
	 */
	public void addField(String fieldName, String fieldType) {
		fields.put(fieldName, fieldType);
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

	public PackageNode getParentNode() {
		return parentNode;
	}

	public String getNodeName() {
		return nodeName;
	}

	public Map<String, String> getMethods() {
		return methods;
	}
	
	public Map<String, String> getFields() {
		return fields;
	}

	public List<String> getVariablesTypes() {
		return new ArrayList<>(variables.values());
	}

	public List<String> getMethodParameterTypes() {
		return methodsParametersTypes;
	}

	public List<String> getMethodsReturnTypes() {
		return new ArrayList<>(getMethods().values());
	}

	public List<String> getFieldsTypes() {
		return new ArrayList<>(getFields().values());
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

	public NodeType getType() {
		return nodeType;
	}

}
