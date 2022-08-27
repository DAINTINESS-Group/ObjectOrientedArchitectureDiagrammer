package model.tree;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**This class is responsible for the implementation of a node in the tree.
 * Each node has a parent node, i.e., the parent package, the path of the source file,
 * the relationships that start from this node
 */
public abstract class Node {
    protected final Path path;
    protected PackageNode parentNode;
    private final List<Relationship> nodeRelationships;

    public Node(Path path) {
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

    public Path getNodesPath() {
        return path;
    }

    public abstract PackageNode getParentNode();

    public abstract String getName();

    public abstract NodeType getType();
}
