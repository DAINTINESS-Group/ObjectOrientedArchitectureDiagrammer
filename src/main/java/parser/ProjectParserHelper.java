package parser;

import model.tree.edge.RelationshipIdentifier;
import model.tree.node.LeafNode;
import model.tree.node.PackageNode;

import java.nio.file.Path;
import java.util.Map;

public interface ProjectParserHelper {

    LeafNode createLeafNode(Path path);

    RelationshipIdentifier createRelationshipIdentifier(Map<Path, PackageNode> packageNodes);

    FileVisitor createFileVisitor();

}
