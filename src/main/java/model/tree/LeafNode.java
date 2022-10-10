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
	private String inheritanceLine[];
	private final Map<String, String> fields;
	private final Map<String, String> methods;
	private final List<String> methodsParametersTypes;

	/**This method is responsible for initializing the nodes structs
	 * @param path the path of Java source file
	 */
	public LeafNode(Path path) {
		super(path);
		methodsParametersTypes = new ArrayList<>();
		fields = new HashMap<>();
		methods = new HashMap<>();
	}

	/**This method is responsible for setting the nodes line that contains the declaration
	 *  of the source file
	 * @param inheritanceLine the Java source file's line holding the information regarding its inheritance
	 */
	public void setInheritanceLine(String[] inheritanceLine) {
		this.inheritanceLine = inheritanceLine;
	}

	/**This method is responsible for adding to the nodes' method parameter types
	 * @param methodParameterTypes the different types of parameters the Java source file's methods take
	 */
	public void addMethodParametersTypes(List<String> methodParameterTypes) {
		for (String methodParameterType: methodParameterTypes) {
			methodsParametersTypes.add(methodParameterType.replaceAll("<", "[").replaceAll(">", "]"));
		}
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
	
	public String[] getInheritanceLine() {
		return inheritanceLine;
	}
	
	public String getName() {
		return path.normalize().toString().substring(path.normalize().toString().lastIndexOf("\\") + 1, path.normalize().toString().lastIndexOf("."));
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

	public NodeType getType() {
		if (inheritanceLine[0].equals("enum")) {
			return NodeType.ENUM;
		}else if (inheritanceLine[0].equals("interface")) {
			return NodeType.INTERFACE;
		}else {
			return NodeType.CLASS;
		}
	}

}
