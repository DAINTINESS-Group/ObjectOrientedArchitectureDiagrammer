package model.diagram;


import model.diagram.graphml.GraphMLLeafNode;
import model.diagram.graphml.GraphMLPackageNode;
import model.tree.node.Node;
import model.tree.node.LeafNode;
import model.tree.node.NodeType;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphNodeCollection {

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

    public StringBuilder convertLeafNodesToGraphML(Map<Integer, List<Double>> nodesGeometry) {
		GraphMLLeafNode graphMLLeafNode = new GraphMLLeafNode();
        for (Map.Entry<Node, Integer> entry: graphNodes.entrySet()) {
            graphMLBuffer.append(graphMLLeafNode.convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue())));
        }
        return graphMLBuffer;
    }

	public StringBuilder convertPackageNodesToGraphML(Map<Integer, List<Double>> nodesGeometry) {
		GraphMLPackageNode graphMLPackageNode = new GraphMLPackageNode();
        for (Map.Entry<Node, Integer> entry: graphNodes.entrySet()) {
            graphMLBuffer.append(graphMLPackageNode.convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue())));
        }
        return graphMLBuffer;
    }

    public Map<String, String> convertNodesToPlantUML() {
    	StringBuilder plantUMLDeclarations = new StringBuilder();
    	plantUMLBuffer = new StringBuilder();
    	for (Node node : graphNodes.keySet()) {
    		StringBuilder plantUMLStringTester = new StringBuilder();
    		plantUMLDeclarations.append(node.getType().toString().toLowerCase()).append(" ").append(node.getName()).append(" {\n");
    		plantUMLStringTester.append(node.getType().toString().toLowerCase()).append(" ").append(node.getName()).append(" {\n");
    		if (node.getType().equals(NodeType.PACKAGE)) {
    			packageFlag = true;
    		}
    		if (!packageFlag) {
    			Map<String, String> nodeFields = ((LeafNode) node).getFields();
            	Map<String, String> fieldsVisibilities = ((LeafNode) node).getFieldVisibilities();
        		for (String fieldName : nodeFields.keySet()) {
        			String fieldType = nodeFields.get(fieldName);
        			String fieldVisibility = visibilityToPlantUML(fieldsVisibilities.get(fieldName));
        			plantUMLDeclarations.append(fieldVisibility).append(fieldName).append(": ").append(fieldType).append("\n");
        			plantUMLStringTester.append(fieldVisibility).append(fieldName).append(": ").append(fieldType).append("\n");
        		}

        		Map<String, String> nodeMethods = ((LeafNode) node).getMethods();
            	Map<String, String> methodsVisibilities = ((LeafNode) node).getMethodVisibilities();
        		Map<String, List<String>> methodNameWithReadyParameters = ((LeafNode) node).getMethodToParameter();
        		for (String methodName : nodeMethods.keySet()) {
        			boolean parametersFlag = false;
        			String methodType = nodeMethods.get(methodName);
            		String methodVisibility = visibilityToPlantUML(methodsVisibilities.get(methodName));
        			plantUMLDeclarations.append(methodVisibility).append(methodName).append("(");
        			plantUMLStringTester.append(methodVisibility).append(methodName).append("(");
        			List<String> resultList = methodNameWithReadyParameters.get(methodName);
        			for (String str : resultList) {
        				parametersFlag = true;
        				plantUMLDeclarations.append(str).append(", ");
        				plantUMLStringTester.append(str).append(", ");
        			}
        			// using its substring TO REMOVE ", " added by the last parameter.
        			if (parametersFlag) {
        				plantUMLDeclarations = new StringBuilder(plantUMLDeclarations.substring(0, plantUMLDeclarations.length() - 2));
        				plantUMLStringTester = new StringBuilder(plantUMLStringTester.substring(0, plantUMLStringTester.length() - 2));
        			}
        			plantUMLDeclarations.append("): ").append(methodType).append("\n");
        			plantUMLStringTester.append("): ").append(methodType).append("\n");
        			
        		}
    		}
    		plantUMLDeclarations.append("}\n");
    		plantUMLStringTester.append("}\n");
    		plantUMLTester.put(node.getName(), plantUMLStringTester.toString());
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

    // public abstract String convertNode(Node node, int nodeId, List<Double> nodesGeometry);
    
    private String visibilityToPlantUML(String visibility) {
		return switch (visibility) {
			case "private" -> "-";
			case "public" -> "+";
			case "protected" -> "#";
			default -> "~";
		};
    }

}
