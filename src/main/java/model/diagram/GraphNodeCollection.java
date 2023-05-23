package model.diagram;


import model.tree.node.Node;
import model.tree.node.LeafNode;
import model.tree.node.ModifierType;
import model.tree.node.NodeType;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GraphNodeCollection {

    protected static final int X_COORDINATE = 0;
    protected static final int Y_COORDINATE = 1;

    private final Map<Node, Integer> graphNodes;
    private final StringBuilder graphMLBuffer;
    private StringBuilder plantUMLBuffer;  
    private final Map<String, String> plantUMLTester;
    private boolean packageFlag = false;
    private int nodeId;

    public GraphNodeCollection() {
        graphNodes = new HashMap<>();
        plantUMLTester = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        nodeId = 0;
    }

    public void populateGraphNodes(List<Node> nodes){
        for (Node node: nodes) {
            graphNodes.put(node, nodeId);
            nodeId++;
        }
    }

    public StringBuilder convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry) {
        for (Map.Entry<Node, Integer> entry: graphNodes.entrySet()) {
            graphMLBuffer.append(convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue())));
        }
        return graphMLBuffer;
    }
    
    public Map<String, String> convertNodesToPlantUML() {
    	StringBuilder plantUMLDeclarations = new StringBuilder();
    	plantUMLBuffer = new StringBuilder();
    	for (Node node : graphNodes.keySet()) {
    		StringBuilder plantUMLDeclaration = new StringBuilder();
    		plantUMLDeclaration.append(node.getType().toString().toLowerCase()).append(" ").append(node.getName()).append(" {\n");
    		if (node.getType().equals(NodeType.PACKAGE)) {
    			packageFlag = true;
    		}
    		if (!packageFlag) {
    			plantUMLDeclaration.append(convertFieldsToPlantUML(node));
    			plantUMLDeclaration.append(convertMethodsToPlantUML(node));
    		}
    		plantUMLDeclaration.append("}\n");
    		plantUMLTester.put(node.getName(), plantUMLDeclaration.toString());
    		plantUMLDeclarations.append(plantUMLDeclaration);
    	}
    	plantUMLBuffer.append(plantUMLDeclarations);
    	return plantUMLTester;
    }

	public Map<Node, Integer> getGraphNodes() {
        return graphNodes;
    }
    
    public boolean getDiagramsChoice() {
    	return packageFlag;
    }

    public String getGraphMLBuffer(){ return graphMLBuffer.toString(); }
    
    public String getPlantUMLBuffer(){ return plantUMLBuffer.toString(); }    

    public abstract String convertNode(Node node, int nodeId, List<Double> nodesGeometry);
    
    private String visibilityToPlantUML(ModifierType visibilityEnum) {
		return switch (visibilityEnum) {
			case PRIVATE -> "-";
			case PUBLIC -> "+";
			case PROTECTED -> "#";
			default -> "~";
		};
    }
    

    private StringBuilder convertFieldsToPlantUML(Node node) {
//    	StringBuilder plantUMLFieldDeclarations = new StringBuilder();
//    	Map<String, String> nodeFields = ((LeafNode) node).getFields();
//    	Map<String, ModifierType> fieldsVisibilities = ((LeafNode) node).getFieldVisibilities();
    	List<String> fieldNames = ((LeafNode) node).getFieldNamesList();
    	List<String> fieldTypes = ((LeafNode) node).getFieldTypesList();
    	List<ModifierType> fieldVisibilitiesList = ((LeafNode) node).getFieldVisibilitiesList();
    	StringBuilder plantUMLFields = new StringBuilder();
    	for (int i=0; i<fieldNames.size();i++) {
			plantUMLFields.append(visibilityToPlantUML(fieldVisibilitiesList.get(i))).append(fieldNames.get(i) + ": ").append(fieldTypes.get(i)).append("\n");
		}
//    	System.out.println("new");
//    	System.out.println(plantUMLFields);
//		for (String fieldName : nodeFields.keySet()) {
//			String fieldType = nodeFields.get(fieldName);
//			String fieldVisibility = visibilityToPlantUML(fieldsVisibilities.get(fieldName));
//			plantUMLFieldDeclarations.append(fieldVisibility).append(fieldName).append(": ").append(fieldType).append("\n");
//		}
//		System.out.println("old");
//		System.out.println(plantUMLFieldDeclarations);
		return plantUMLFields;
	}
    
    private StringBuilder convertMethodsToPlantUML(Node node) {
//    	StringBuilder plantUMLMethodsDeclarations = new StringBuilder();
//		Map<String, String> nodeMethods = ((LeafNode) node).getMethods();
//    	Map<String, ModifierType> methodsVisibilities = ((LeafNode) node).getMethodVisibilities();
		List<String> methodNames = ((LeafNode) node).getMethodNamesList();
		List<String> methodReturnTypes = ((LeafNode) node).getMethodReturnTypesList();
		List<ModifierType> methodVisibilitiesList = ((LeafNode) node).getMethodVisibilitiesList();
		List<StringBuilder> methodParameters = ((LeafNode) node).getMethodParametersList();
		StringBuilder plantUMLMethods = new StringBuilder();
		for (int i=0; i<methodNames.size();i++) {
			plantUMLMethods.append(visibilityToPlantUML(methodVisibilitiesList.get(i))).append(methodNames.get(i)).append("(").append(methodParameters.get(i) + "): ").append(methodReturnTypes.get(i)).append("\n");
		}
//		// System.out.println(plantUMLMethods);
//		for (String methodName : nodeMethods.keySet()) {
//			String methodType = nodeMethods.get(methodName);
//    		String methodVisibility = visibilityToPlantUML(methodsVisibilities.get(methodName));
//    		// plantUMLMethods.append(methodVisibility).append(methodName).append("(");
//    		plantUMLMethodsDeclarations.append(methodVisibility).append(methodName).append("(");
//    		plantUMLMethodsDeclarations.append(convertMethodParametersToPlantUML(node, methodName));
//			// plantUMLMethods.append("): ").append(methodType).append("\n");
//			plantUMLMethodsDeclarations.append("): ").append(methodType).append("\n");
//			
//		}
//		// System.out.println(plantUMLMethodsDeclarations);
    	return plantUMLMethods;
    }
    
//    private StringBuilder convertMethodParametersToPlantUML(Node node, String methodName) {
//    	StringBuilder plantUMLMethodParameters = new StringBuilder();
//		boolean parametersFlag = false;
//		Map<String, List<String>> methodNameWithReadyParameters = ((LeafNode) node).getMethodToParameter();
//    	List<String> resultList = methodNameWithReadyParameters.get(methodName);
//		for (String str : resultList) {
//			parametersFlag = true;
//			// plantUMLMethods.append(str).append(", ");
//			plantUMLMethodParameters.append(str).append(", ");
//		}
//		// using its substring TO REMOVE ", " added by the last parameter.
//		if (parametersFlag) {
//			// plantUMLMethods = new StringBuilder(plantUMLMethods.substring(0, plantUMLMethods.length() - 2));
//			plantUMLMethodParameters = new StringBuilder(plantUMLMethodParameters.substring(0, plantUMLMethodParameters.length() - 2));
//		}
//    	return plantUMLMethodParameters;
//    }

}
