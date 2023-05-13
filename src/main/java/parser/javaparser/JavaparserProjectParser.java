package parser.javaparser;

import model.tree.javaparser.JavaparserLeafNode;
import model.tree.javaparser.JavaparserRelationshipIdentifier;
import model.tree.node.LeafNode;
import model.tree.node.PackageNode;
import parser.FileVisitor;
import parser.ProjectParserHelper;

import java.nio.file.Path;
import java.util.Map;

public class JavaparserProjectParser implements ProjectParserHelper {

    @Override
    public LeafNode createLeafNode(Path path) {
        return new JavaparserLeafNode(path);
    }

    @Override
    public void createRelationshipIdentifier(Map<Path, PackageNode> packageNodes) {
        new JavaparserRelationshipIdentifier(packageNodes);
    }

    @Override
    public FileVisitor createFileVisitor() {
        return new JavaparserFileVisitor();
    }
}
