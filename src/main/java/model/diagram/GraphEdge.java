package model.diagram;

import model.tree.Node;
import model.tree.Relationship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GraphEdge {

    protected final Map<Relationship, Integer> graphEdges;
    protected Map<Node, Integer> graphNodes;
    protected final StringBuilder graphMLBuffer;
    private int edgeId;

    public GraphEdge() {
        graphEdges = new HashMap<>();
        graphMLBuffer = new StringBuilder();
        edgeId = 0;
    }

    public void populateGraphEdges(List<Node> nodes) {
        for (Node node: nodes) {
            generateEdge(node);
        }
    }

    public void generateEdge(Node node) {
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

    public String getGraphMLBuffer() { return graphMLBuffer.toString(); }

    public abstract void convertEdgesToGraphML();

}
