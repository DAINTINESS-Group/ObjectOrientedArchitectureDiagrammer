package parser.ast;

import static proguard.classfile.JavaConstants.JAVA_FILE_EXTENSION;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.ast.tree.LeafNode;
import parser.ast.tree.PackageNode;
import parser.ast.tree.Relationship;

/**
 * This parser is responsible for creating the AST of the given source files and identifying the
 * relationships between the {@link LeafNode}s of the tree.
 */
public class ASTParser {
    private static final Logger logger = LogManager.getLogger(ASTParser.class);

    private final Map<Path, PackageNode> packageNodes = new HashMap<>();

    public Map<Path, PackageNode> parsePackage(Path sourcePackagePath) {
        PackageNode sourcePackage = PackageNode.from(sourcePackagePath);
        packageNodes.put(sourcePackage.getPath(), sourcePackage);
        parseFolder(sourcePackage);

        return packageNodes;
    }

    public Map<LeafNode, Set<Relationship<LeafNode>>> createRelationships(
            Map<Path, PackageNode> packageNodes) {
        ASTRelationshipIdentifier relationshipIdentifier = new ASTRelationshipIdentifier();

        return relationshipIdentifier.createLeafNodesRelationships(packageNodes);
    }

    public Map<PackageNode, Set<Relationship<PackageNode>>> identifyPackageNodeRelationships(
            Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships) {
        ASTRelationshipIdentifier relationshipIdentifier = new ASTRelationshipIdentifier();

        return relationshipIdentifier.identifyPackageNodeRelationships(leafNodeRelationships);
    }

    private void parseFolder(PackageNode currentNode) {
        try (DirectoryStream<Path> filesStream = Files.newDirectoryStream(currentNode.getPath())) {
            for (Path path : filesStream) {
                if (Files.isDirectory(path)) {
                    PackageNode subNode = PackageNode.from(currentNode, path);
                    packageNodes.put(subNode.getPath(), subNode);
                    currentNode.getSubNodes().put(subNode.getPath(), subNode);
                    parseFolder(subNode);
                } else if (isJavaSourceFile(path)) {
                    FileVisitor fileVisitor = new FileVisitor(currentNode, path);
                    LeafNode leafNode = fileVisitor.createAST();
                    currentNode.getLeafNodes().put(leafNode.nodeName(), leafNode);
                }
            }
        } catch (Exception e) {
            logger.error(
                    "Failed to open a directory stream for path: {}",
                    currentNode.getPath().toAbsolutePath());
            throw new RuntimeException(e);
        }
    }

    private static boolean isJavaSourceFile(Path filePath) {
        return filePath.normalize().toString().toLowerCase().endsWith(JAVA_FILE_EXTENSION);
    }
}
