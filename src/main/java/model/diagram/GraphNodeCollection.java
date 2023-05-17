package model.diagram;


import model.tree.node.Node;
import model.tree.node.LeafNode;


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
        	System.out.println("ML" + entry.getKey() + entry.getValue());
        	
            graphMLBuffer.append(convertNode(entry.getKey(), entry.getValue(), nodesGeometry.get(entry.getValue())));
        }
        return graphMLBuffer;
    }
    
    public Map<String, String> convertNodesToPlantUML() {
    	String plantUMLDeclarations = "";
    	plantUMLBuffer = new StringBuilder();
    	for (Node node : graphNodes.keySet()) {
    		String plantUMLStringTester = "";
    		plantUMLDeclarations += node.getType().toString().toLowerCase() + " " + node.getName() + " {\n";
    		plantUMLStringTester += node.getType().toString().toLowerCase() + " " + node.getName() + " {\n";
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
        			plantUMLStringTester += fieldVisibility + fieldName + ": " + fieldType + "\n";
        		}
        		Map<String, String> nodeMethods = ((LeafNode) node).getMethods();
            	Map<String, String> methodsVisibilities = ((LeafNode) node).getMethodVisibilities();
        		Map<String, List<String>> methodNameWithReadyParameters = ((LeafNode) node).getMethodToParameter();
        		for (String methodName : nodeMethods.keySet()) {
        			boolean parametersFlag = false;
        			String methodType = nodeMethods.get(methodName).toString();
            		String methodVisibility = visibilityToPlantUML(methodsVisibilities.get(methodName));
        			plantUMLDeclarations += methodVisibility + methodName + "(";
        			plantUMLStringTester += methodVisibility + methodName + "(";
        			List<String> resultList = methodNameWithReadyParameters.get(methodName);
        			for (String str : resultList) {
        				parametersFlag = true;
        				plantUMLDeclarations += str + ", ";
        				plantUMLStringTester += str + ", ";
        			}
        			// using its substring TO REMOVE ", " added by the last parameter.
        			if (parametersFlag) {
        				plantUMLDeclarations = plantUMLDeclarations.substring(0, plantUMLDeclarations.length() - 2);
        				plantUMLStringTester = plantUMLStringTester.substring(0, plantUMLStringTester.length() - 2);
        			}
        			plantUMLDeclarations += "): " + methodType + "\n";
        			plantUMLStringTester += "): " + methodType + "\n";
        			
        		}
    		}
    		plantUMLDeclarations += "}\n";
    		plantUMLStringTester += "}\n";
    		plantUMLTester.put(node.getName(), plantUMLStringTester);
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
    
    private String visibilityToPlantUML(String visibility) {
    	if(null == visibility)
    		return "~";
    	switch (visibility) {
			case "private":
				return "-";
			case "public":
				return "+";
			case "protected":
				return "#";
			default: 
				return "~";
    	}
    	//return "";
    }

}
