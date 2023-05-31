package model.tree.node;

import org.javatuples.Triplet;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**This class is responsible for the implementation of a leaf node in the tree.
 * Each node has a parent node(the parent package), the path of the source file,
 * the branches that start from that node and also the field/method/method parameter types
 */
public abstract class LeafNode extends Node{
	private final List<Triplet<String, String, ModifierType>> fields;
	private final Map<Triplet<String, String, ModifierType>, Map<String, String>> methods;
	private int methodId;

	/**This method is responsible for initializing the nodes structs
	 * @param path the path of Java source file
	 */
	public LeafNode(Path path) {
		super(path);
		fields = new ArrayList<>();
		methods = new HashMap<>();
		methodId = 0;
	}

	/**This method is responsible for adding the nodes' method's name, return type, modifier type & its parameters
	 * that include the name and the type
	 * @param name the method's name
	 * @param returnType the method's return type
	 * @param modifierType the method's modifier type
	 * @param parameters the method's parameters
	 */
	public void addMethod(String name, String returnType, ModifierType modifierType, Map<String, String> parameters) {
		methods.put(new Triplet<>(name + "$" + methodId, returnType, modifierType), parameters);
		methodId++;
	}

	/**This method is responsible for adding the nodes' field's name, type & modifier type
	 * @param fieldName the field's name
	 * @param fieldType the field's type
	 * @param modifierType the field's modifier type
	 */
	public void addField(String fieldName, String fieldType, ModifierType modifierType) {
		fields.add(new Triplet<>(fieldName, fieldType, modifierType));
	}

	public PackageNode getParentNode() {
		return parentNode;
	}

	public Map<Triplet<String, String, ModifierType>, Map<String, String>> getMethods(){
		return methods;
	}

	public List<Triplet<String, String, ModifierType>> getMethodNamesAndTypes() {
		return new ArrayList<>(methods.keySet());
	}

	public List<String> getMethodsNames() {
		return methods.keySet().stream().map(Triplet::getValue0).collect(Collectors.toList());
	}

	public List<String> getMethodsReturnTypes() {
		return methods.keySet().stream().map(Triplet::getValue1).collect(Collectors.toList());
	}

	public List<ModifierType> getMethodVisibilities(){
		return methods.keySet().stream().map(Triplet::getValue2).collect(Collectors.toList());
	}

	public List<String> getMethodParameterTypes() {
		List<String> parameterTypes = new ArrayList<>();
		methods.forEach((method, parameters) -> parameterTypes.addAll(new ArrayList<>(parameters.values())));
		return parameterTypes;
	}

	public List<Triplet<String, String, ModifierType>> getFields() {
		return fields;
	}

	public List<String> getFieldsNames(){
		return fields.stream().map(Triplet::getValue0).collect(Collectors.toList());
	}

	public List<String> getFieldsTypes(){
		return fields.stream().map(Triplet::getValue1).collect(Collectors.toList());
	}

	public List<ModifierType> getFieldVisibilities(){
		return fields.stream().map(Triplet::getValue2).collect(Collectors.toList());
	}

	public abstract NodeType getType();

	public abstract String getName();

}
