package parser.tree;

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
public abstract class LeafNode {
	protected final Path path;
	private PackageNode parentNode;
	private final List<Relationship<LeafNode>> leafNodeRelationships;
	private final List<Triplet<String, String, ModifierType>> fields;
	private final Map<Triplet<String, String, ModifierType>, Map<String, String>> methods;
	private int methodId;

	public LeafNode(Path path) {
		this.path = path;
		leafNodeRelationships = new ArrayList<>();
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

	public void setParentNode(PackageNode p) {
		this.parentNode = p;
	}

	public void addLeafNodeRelationship(Relationship<LeafNode> relationship) {
		leafNodeRelationships.add(relationship);
	}

	public List<Relationship<LeafNode>> getLeafNodeRelationships() {
		return leafNodeRelationships;
	}

	public Path getLeafNodesPath() {
		return path;
	}

	public PackageNode getParentNode() {
		return parentNode;
	}

	public Map<Triplet<String, String, ModifierType>, Map<String, String>> getMethods(){
		return methods;
	}

	public List<String> getMethodsReturnTypes() {
		return methods.keySet().stream().map(Triplet::getValue1).collect(Collectors.toList());
	}

	public List<String> getMethodParameterTypes() {
		List<String> parameterTypes = new ArrayList<>();
		methods.forEach((method, parameters) -> parameterTypes.addAll(new ArrayList<>(parameters.values())));
		return parameterTypes;
	}

	public List<Triplet<String, String, ModifierType>> getFields() {
		return fields;
	}

	public List<String> getFieldsTypes(){
		return fields.stream().map(Triplet::getValue1).collect(Collectors.toList());
	}

	public abstract NodeType getType();

	public abstract String getName();

}
