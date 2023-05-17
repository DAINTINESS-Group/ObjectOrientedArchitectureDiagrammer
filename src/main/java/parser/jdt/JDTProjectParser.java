package parser.jdt;

import model.tree.edge.RelationshipIdentifier;
import model.tree.jdt.JDTLeafNode;
import model.tree.jdt.JDTRelationshipIdentifier;
import model.tree.node.LeafNode;
import model.tree.node.PackageNode;
import parser.FileVisitor;
import parser.ProjectParserHelper;

import java.nio.file.Path;
import java.util.Map;

public class JDTProjectParser implements ProjectParserHelper {

    @Override
    public LeafNode createLeafNode(Path path) {
        return new JDTLeafNode(path);
    }

    @Override
    public RelationshipIdentifier createRelationshipIdentifier(Map<Path, PackageNode> packageNodes) {
        return new JDTRelationshipIdentifier(packageNodes);
    }

    @Override
    public FileVisitor createFileVisitor() {
        return new JDTFileVisitor();
    }
}
