package model.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    protected final String path;
    protected PackageNode parentNode;
    private final List<Relationship> nodeRelationships;

    public Node(String path) {
        this.path = path;
        nodeRelationships = new ArrayList<>();
    }

    public void setParentNode(PackageNode p) {
        this.parentNode = p;
    }

    public void addNodeRelationship(Relationship relationship) {
        nodeRelationships.add(relationship);
    }

    public List<Relationship> getNodeRelationships() {
        return nodeRelationships;
    }

    public String getNodesPath() {
        return path;
    }

    public abstract PackageNode getParentNode();

    public abstract String getName();

    public abstract NodeType getType();
}
