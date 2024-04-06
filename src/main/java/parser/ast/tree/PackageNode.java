package parser.ast.tree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is responsible for the representation of a package node in the tree. Each node has a
 * parent node (the parent package), the path of the package folder, the node's children(the
 * sub-packages), the node's leaves(the Java source files inside the current package) and a flag to
 * identify if a package is empty or not.
 */
public final class PackageNode {
    private static final Logger logger = LogManager.getLogger(PackageNode.class);

    private final Map<Path, PackageNode> subNodes = new HashMap<>();
    private final Map<String, LeafNode> leafNodes = new HashMap<>();
    private final Path path;
    private final PackageNode parentNode;
    private final boolean isValid;

    /** Creates a new {@link PackageNode} that is in the root of the package structure. */
    public static PackageNode from(Path path) {
        return new PackageNode(new PackageNode(null, Paths.get(""), false), path, isValid(path));
    }

    /** Creates a new {@link PackageNode} with the given parent node. */
    public static PackageNode from(PackageNode parentNode, Path path) {
        return new PackageNode(parentNode, path, isValid(path));
    }

    private PackageNode(PackageNode parentNode, Path path, boolean isValid) {
        this.parentNode = parentNode;
        this.path = path;
        this.isValid = isValid;
    }

    private static boolean isValid(Path path) {
        boolean isValid;

        try (Stream<Path> filesStream = Files.list(path)) {
            isValid =
                    filesStream.anyMatch(
                            filePath ->
                                    filePath.normalize()
                                            .toString()
                                            .toLowerCase()
                                            .endsWith(".java"));
        } catch (IOException e) {
            logger.error("Failed to stream file in path: {}", path);
            throw new RuntimeException(e);
        }
        return isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public Path getPath() {
        return path;
    }

    public PackageNode getParentNode() {
        return parentNode;
    }

    public Map<Path, PackageNode> getSubNodes() {
        return subNodes;
    }

    public Map<String, LeafNode> getLeafNodes() {
        return leafNodes;
    }

    public String getNodeName() {
        if (!doesParentNodeExist()) {
            return path.getFileName().toString();
        }
        return String.join(".", getParentNode().getNodeName(), path.getFileName().toString());
    }

    private boolean doesParentNodeExist() {
        return getParentNode() != null
                && !getParentNode().getPath().normalize().toString().isEmpty();
    }

    public NodeType getNodeType() {
        return NodeType.PACKAGE;
    }
}
