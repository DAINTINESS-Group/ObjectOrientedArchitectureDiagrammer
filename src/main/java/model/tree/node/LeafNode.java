package model.tree.node;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**This class is responsible for the implementation of a leaf node in the tree.
 * Each node has a parent node(the parent package), the path of the source file,
 * the branches that start from that node and also the field/method/method parameter types
 */
public abstract class LeafNode extends Node{
	private final Map<String, String> fields;
	private final Map<String, String> methods;
	protected final List<String> methodsParametersTypes;

	/**This method is responsible for initializing the nodes structs
	 * @param path the path of Java source file
	 */
	public LeafNode(Path path) {
		super(path);
		methodsParametersTypes = new ArrayList<>();
		fields = new HashMap<>();
		methods = new HashMap<>();
	}

	/**This method is responsible for adding to the nodes' method parameter types
	 * @param methodParameterType the type of parameter the node's methods take
	 */
	public void addMethodParameterType(String methodParameterType) {
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

	public PackageNode getParentNode() {
		return parentNode;
	}

	public Map<String, String> getMethods() {
		return methods;
	}
	
	public Map<String, String> getFields() {
		return fields;
	}
	
	public List<String> getMethodParameterTypes() {
		return methodsParametersTypes;
	}

	public List<String> getMethodsReturnTypes() { return new ArrayList<>(getMethods().values());}

	public List<String> getFieldsTypes(){ return new ArrayList<>(getFields().values());}

	public abstract NodeType getType();

	public abstract String getName();

}
