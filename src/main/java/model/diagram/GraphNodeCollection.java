package model.diagram;

import model.diagram.graphml.GraphMLLeafNode;
import model.diagram.graphml.GraphMLPackageNode;
import model.diagram.plantuml.PlantUMLLeafNode;
import model.diagram.plantuml.PlantUMLPackageNode;
import model.tree.node.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphNodeCollection {

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
    
	public Map<String, String> convertClassNodesToPlantUML() {
        PlantUMLLeafNode plantUMLLeafNode = new PlantUMLLeafNode();
        plantUMLBuffer = new StringBuilder();
    	for (Node node : graphNodes.keySet()) {
    		String convertedNode = plantUMLLeafNode.convertPlantNode(node);
    		plantUMLBuffer.append(convertedNode);
    		plantUMLTester.put(node.getName(), convertedNode);
    	}
    	return plantUMLTester;
	}

	public Map<String, String> convertPackageNodesToPlantUML() {
        PlantUMLPackageNode plantUMLPackageNode = new PlantUMLPackageNode();
    	plantUMLBuffer = new StringBuilder();
    	for (Node node : graphNodes.keySet()) {
    		String convertedNode = plantUMLPackageNode.convertPlantNode(node);
    		plantUMLBuffer.append(convertedNode).append("\n");
    		plantUMLTester.put(node.getName(), convertedNode);
    	}
    	return plantUMLTester;
	}

	public Map<Node, Integer> getGraphNodes() {
        return graphNodes;
    }

    public String getGraphMLBuffer(){ return graphMLBuffer.toString(); }
    
    public String getPlantUMLBuffer(){ return plantUMLBuffer.toString(); }    

}
