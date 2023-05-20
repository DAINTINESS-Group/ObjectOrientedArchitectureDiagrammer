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
	private final Map<String, List<String>> methodToParameter;
	private final Map<String, ModifierType> methodVisibilities;
	private final Map<String, ModifierType> fieldVisibilities;
	private final Map<String, String> fields;
	private final Map<String, String> methods;
	private final List<String> methodNames;
	private final List<String> methodReturnTypes;
	private final List<StringBuilder> methodParameters;
	private final List<ModifierType> methodVisibilitiesList;
	private final List<ModifierType> fieldVisibilitiesList;
	private final List<String> fieldNamesList;
	private final List<String> fieldTypesList;
	protected final List<String> methodsParametersTypes;

	/**This method is responsible for initializing the nodes structs
	 * @param path the path of Java source file
	 */
	public LeafNode(Path path) {
		super(path);
		methodsParametersTypes = new ArrayList<>();
		fields = new HashMap<>();
		methods = new HashMap<>();
		methodToParameter = new HashMap<>();
		methodVisibilities = new HashMap<>();
		fieldVisibilities = new HashMap<>();
		methodNames = new ArrayList<>();
		methodReturnTypes = new ArrayList<>();
		methodParameters = new ArrayList<>();
		methodVisibilitiesList = new ArrayList<>();
		fieldVisibilitiesList = new ArrayList<>();
		fieldNamesList = new ArrayList<>();
		fieldTypesList = new ArrayList<>();
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
		// System.out.println(methodName + " " + methodReturnType);
		methods.put(methodName, methodReturnType);
		methodNames.add(methodName);
		methodReturnTypes.add(methodReturnType);
	}

	/**This method is responsible for adding the nodes' fields names types
	 * @param fieldName the field's name
	 * @param fieldType the field's type
	 */
	public void addField(String fieldName, String fieldType) {
		fields.put(fieldName, fieldType);
		fieldNamesList.add(fieldName);
		fieldTypesList.add(fieldType);
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
	
//	public void addFieldVisibility(String fieldName, String fieldVisibility) {
//		fieldVisibilities.put(fieldName, fieldVisibility);
//	}
	
	public void addFieldVisibility(String fieldName, ModifierType fieldVisibility) {
		fieldVisibilities.put(fieldName, fieldVisibility);
		fieldVisibilitiesList.add(fieldVisibility);
	}
	
//	public void addMethodVisibility(String methodName, String methodVisibility) {
//		methodVisibilities.put(methodName, methodVisibility);
//		methodVisibilitiesList.add(methodVisibility);
//	}
	
	public void addMethodVisibility(String methodName, ModifierType methodVisibility) {
		methodVisibilities.put(methodName, methodVisibility);
		methodVisibilitiesList.add(methodVisibility);
	}
	
	public Map<String, ModifierType> getFieldVisibilities(){
		return fieldVisibilities;
	}
	
	public Map<String, ModifierType> getMethodVisibilities(){
		return methodVisibilities;
	}
	
	public void addForPlantUML(String methodName, List<String> parametersTypes, List<String> parametersNames) {
		List<String> stringMergerList = new ArrayList<>();
		StringBuilder stringMergerString = new StringBuilder();
		for (int i = 0; i < parametersTypes.size(); i++) {
			stringMergerList.add(parametersTypes.get(i) + " " + parametersNames.get(i));
			if (i == parametersTypes.size() - 1) {
				stringMergerString.append(parametersTypes.get(i) + " " + parametersNames.get(i));
			}else {
				stringMergerString.append(parametersTypes.get(i) + " " + parametersNames.get(i) + ", ");
			}
		}
		methodToParameter.put(methodName, stringMergerList);
		methodParameters.add(stringMergerString);
	}
	
	public List<String> getMethodNamesList(){
		return methodNames;
	}
	
	public List<String> getMethodReturnTypesList(){
		return methodReturnTypes;
	}
	
	public List<ModifierType> getMethodVisibilitiesList(){
		return methodVisibilitiesList;
	}
	
	public List<StringBuilder> getMethodParametersList(){
		return methodParameters;
	}
	
	public List<String> getFieldNamesList(){
		return fieldNamesList;
	}
	
	public List<String> getFieldTypesList(){
		return fieldTypesList;
	}
	
	public List<ModifierType> getFieldVisibilitiesList(){
		return fieldVisibilitiesList;
	}
	
	public Map<String, List<String>> getMethodToParameter(){
		return methodToParameter;
	}

	public List<String> getMethodsReturnTypes() { return new ArrayList<>(getMethods().values());}

	public List<String> getFieldsTypes(){ return new ArrayList<>(getFields().values());}

	public abstract NodeType getType();

	public abstract String getName();

}
