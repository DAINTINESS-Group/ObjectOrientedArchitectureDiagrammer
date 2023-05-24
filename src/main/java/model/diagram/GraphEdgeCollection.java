package model.diagram;

import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLPackageEdge;
import model.tree.node.Node;
import model.tree.edge.Relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphEdgeCollection {

    private final Map<Relationship, Integer> graphEdges;
    protected Map<Node, Integer> graphNodes;
    private final StringBuilder graphMLBuffer;
    private StringBuilder plantUMLBuffer;
    private final List<String> plantUMLTester;
    private int edgeId;

    public GraphEdgeCollection(Map<Node, Integer> graphNodes) {
        this.graphNodes = graphNodes;
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
    		plantUMLBuffer.append(convertPlantEdge(relationship) + "\n");
    		plantUMLTester.add(convertPlantEdge(relationship));
		}
    	return plantUMLTester;
    }

    public String getGraphMLBuffer() { return graphMLBuffer.toString(); }

    public String getPlantUMLBuffer() { return plantUMLBuffer.toString(); }
    
    public StringBuilder convertLeafEdgesToGraphML(){
        GraphMLLeafEdge graphMLLeafEdge = new GraphMLLeafEdge(graphNodes);
        for (Map.Entry<Relationship, Integer> entry: graphEdges.entrySet()) {
            graphMLBuffer.append(graphMLLeafEdge.convertEdge(entry.getKey(), entry.getValue()));
        }
        return graphMLBuffer;
    }

    public StringBuilder convertPackageEdgesToGraphML(){
        GraphMLPackageEdge graphMLPackageEdge = new GraphMLPackageEdge(graphNodes);
        for (Map.Entry<Relationship, Integer> entry: graphEdges.entrySet()) {
            graphMLBuffer.append(graphMLPackageEdge.convertEdge(entry.getKey(), entry.getValue()));
        }
        return graphMLBuffer;
    }
}
