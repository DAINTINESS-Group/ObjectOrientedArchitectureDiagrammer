package model.diagram;

import model.tree.LeafNode;
import model.tree.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GraphNodeCollection {

    protected static final int X_COORDINATE = 0;
    protected static final int Y_COORDINATE = 1;

    private final Map<Node, Integer> graphNodes;
    private final StringBuilder graphMLBuffer;
    private final StringBuilder plantUMLBuffer;  
    private final Map<Map<String, String>, List<String>> plantUMLTester;
    private boolean packageFlag;
    private int nodeId;

    public GraphNodeCollection() {
        graphNodes = new HashMap<>();
        plantUMLTester = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        plantUMLBuffer = new StringBuilder();
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
        	System.out.println("ML" + entry.getKey() + entry.getValue());
        	
            graphMLBuffer.append(convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue())));
        }
        return graphMLBuffer;
    }
    
    public void convertNodesToPlantUML() {
    	boolean packageFlag = false;
    	String plantUMLDeclarations = "";
    	for (Node node : graphNodes.keySet()) {
    		plantUMLDeclarations += node.getType().toString().toLowerCase() + " " + node.getName() + " {\n";
    		if (node.getType().toString() == "PACKAGE") {
    			packageFlag = true;
    		}
    		if (!packageFlag) {
    			Map<String, String> nodeFields = ((LeafNode) node).getFields();
            	Map<String, String> fieldsVisibilities = ((LeafNode) node).getFieldVisibilities();
        		for (String fieldName : nodeFields.keySet()) {
        			String fieldType = nodeFields.get(fieldName).toString();
        			String fieldVisibility = visibilityToPlantUML(fieldsVisibilities.get(fieldName));
        			plantUMLDeclarations += fieldVisibility + fieldName + ": " + fieldType + "\n";
        		}
        		Map<String, String> nodeMethods = ((LeafNode) node).getMethods();
            	Map<String, String> methodsVisibilities = ((LeafNode) node).getMethodVisibilities();
        		Map<String, List<String>> methodNameWithReadyParameters = ((LeafNode) node).getMethodToParameter();
        		for (String methodName : nodeMethods.keySet()) {
        			boolean parametersFlag = false;
        			String methodType = nodeMethods.get(methodName).toString();
            		String methodVisibility = visibilityToPlantUML(methodsVisibilities.get(methodName));
        			plantUMLDeclarations += methodVisibility + methodName + "(";
        			List<String> resultList = methodNameWithReadyParameters.get(methodName);
        			for (String str : resultList) {
        				parametersFlag = true;
        				plantUMLDeclarations += str + ", ";
        			}
        			// using its substring TO REMOVE ", " added by the last parameter.
        			if (parametersFlag) {
        				plantUMLDeclarations = plantUMLDeclarations.substring(0, plantUMLDeclarations.length() - 2);
        			}
        			plantUMLDeclarations += "): " + methodType + "\n";
        		}
    		}
    		plantUMLDeclarations += "}\n";
    	}
    	plantUMLBuffer.append(plantUMLDeclarations);
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
    
    private String visibilityToPlantUML(String Visibility) {
    	switch (Visibility) {
			case "private":
				return "-";
			case "public":
				return "+";
			case "protected":
				return "#";
			case "default":
				return "~";
    	}
    	return "";
    }

}
