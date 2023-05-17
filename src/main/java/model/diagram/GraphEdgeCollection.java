package model.diagram;

import model.tree.node.Node;
import model.tree.edge.Relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GraphEdgeCollection {

    private final Map<Relationship, Integer> graphEdges;
    protected Map<Node, Integer> graphNodes;
    private final StringBuilder graphMLBuffer;
    private StringBuilder plantUMLBuffer;
    private final List<String> plantUMLTester;
    private int edgeId;

    public GraphEdgeCollection() {
        graphEdges = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        plantUMLTester = new ArrayList<>();
        edgeId = 0;
    }

    public void populateGraphEdges(List<Node> nodes) {
        for (Node node: nodes) {
            generateEdge(node);
        }
    }

    private void generateEdge(Node node) {
        for (Relationship relationship: node.getNodeRelationships()) {
            if (areEdgesNodesInTheChosenClasses(relationship)) {
                addEdge(relationship);
            }
        }
    }

    private boolean areEdgesNodesInTheChosenClasses(Relationship relationship) {
        return graphNodes.containsKey(relationship.getEndingNode());
    }

    private void addEdge(Relationship relationship) {
        graphEdges.put(relationship, edgeId);
        edgeId++;
    }

    public void setGraphNodes(Map<Node, Integer> graphNodes){
        this.graphNodes = graphNodes;
    }

    public Map<Relationship, Integer> getGraphEdges() {
        return graphEdges;
    }
    
    public List<String> convertEdgesToPlantUML() {
        plantUMLBuffer = new StringBuilder();
    	for ( Relationship relationship : graphEdges.keySet()) {
    		// Map<String, String> nodesHashMap = new HashMap<>();
    		plantUMLBuffer.append(relationship.getStartingNode().getName() + " ");
    		plantUMLBuffer.append(transformPlantUMLRelationship(relationship.getRelationshipType().toString()) + " ");
    		plantUMLBuffer.append(relationship.getEndingNode().getName() + "\n");
    		// nodesHashMap.put(relationship.getStartingNode().getName(), relationship.getEndingNode().getName());
    		plantUMLTester.add(relationship.getStartingNode().getName() + " " + transformPlantUMLRelationship(relationship.getRelationshipType().toString()) + " " + relationship.getEndingNode().getName());
		}
    	return plantUMLTester;
    	
    }

    public String getGraphMLBuffer() { return graphMLBuffer.toString(); }

    public String getPlantUMLBuffer() { return plantUMLBuffer.toString(); }
    
    public StringBuilder convertEdgesToGraphML(){
        for (Map.Entry<Relationship, Integer> entry: graphEdges.entrySet()) {
            graphMLBuffer.append(convertEdge(entry.getKey(), entry.getValue()));
        }
        return graphMLBuffer;
    }

    public abstract String convertEdge(Relationship relationship, int edgeId);
    
    private String transformPlantUMLRelationship(String Relationship) {
    	switch (Relationship) {
    		case "EXTENSION":
    			return "--|>";
    		case "COMPOSITION":
    			return "--*";
    		case "AGGREGATION":
    			return "--o";
    		case "DEPENDENCY":
    			return "..>";
    		case "IMPLEMENTATION":
    			return "..|>";
    		case "ASSOCIATION":
    			return "-->";
    		case "SELFREFERENCE": // SELF-REFERENCING
    			return "";		// A -- A , SAME CLASS WITH -- IN BETWEEN
    	}
    	return "";
    }

}
