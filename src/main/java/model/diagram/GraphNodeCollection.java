package model.diagram;

import model.tree.node.Node;

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
    
	public Map<String, String> convertClassNodesToPlantUML() {
    	plantUMLBuffer = new StringBuilder();
    	for (Node node : graphNodes.keySet()) {
    		String convertedNode = convertPlantNode(node);
    		plantUMLBuffer.append(convertedNode);
    		plantUMLTester.put(node.getName(), convertedNode);
    	}
    	return plantUMLTester;
	}

	public Map<String, String> convertPackageNodesToPlantUML() {
    	plantUMLBuffer = new StringBuilder();
    	for (Node node : graphNodes.keySet()) {
    		String convertedNode = convertPlantNode(node);
    		plantUMLBuffer.append(convertedNode + "\n");
    		plantUMLTester.put(node.getName(), convertedNode);
    	}
    	return plantUMLTester;
	}

	public Map<Node, Integer> getGraphNodes() {
        return graphNodes;
    }
	
    public String getGraphMLBuffer(){ return graphMLBuffer.toString(); }
    
    public String getPlantUMLBuffer(){ return plantUMLBuffer.toString(); }    

    public abstract String convertNode(Node node, int nodeId, List<Double> nodesGeometry);
    
    public abstract String convertPlantNode(Node node);

}
