package parser.tree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This class is responsible for the representation of a package node in the tree.
 * Each has node has a parent node(the parent package), the path of the package folder,
 * the nodes children(the sub packages), the nodes leaves(the Java source files inside the current package),
 * a flag to identify if a package is empty or not.
 */
public final class PackageNode
{
    private static final Logger logger = LogManager.getLogger(PackageNode.class);

    private final Map<Path, PackageNode> subNodes;
    private final Map<String, LeafNode>  leafNodes;
    private final Path                   path;
    private final PackageNode            parentNode;
    private final boolean                isValid;


    public PackageNode(Path path)
    {
        this.path  = path;
        parentNode = new PackageNode(null, Paths.get(""));
        subNodes   = new HashMap<>();
        leafNodes  = new HashMap<>();
        isValid    = isValid(path);
    }


    public PackageNode(PackageNode parentNode, Path path)
    {
        this.parentNode = parentNode;
        this.path       = path;
        subNodes        = new HashMap<>();
        leafNodes       = new HashMap<>();
        isValid         = isValid(path);
    }


    private static boolean isValid(Path path)
    {
        boolean isValid;

        try (Stream<Path> filesStream = Files.list(path))
        {
            isValid = filesStream.anyMatch(filePath -> filePath
                .normalize()
                .toString()
                .toLowerCase()
                .endsWith(".java"));
        }
        catch (IOException e)
        {
            logger.error("Failed to stream file in path: {}", path);
            throw new RuntimeException(e);
        }
        return isValid;
    }


    private static Path getPath(PackageNode parentNode, Path path)
    {
        return Paths.get(String.format("%s/%s",
                                       parentNode.getPath().normalize(),
                                       path.toFile().getName()));
    }


    public boolean isValid()
    {
        return isValid;
    }


    public Path getPath()
    {
        return path;
    }


    public PackageNode getParentNode()
    {
        return parentNode;
    }


    public Map<Path, PackageNode> getSubNodes()
    {
        return subNodes;
    }


    public Map<String, LeafNode> getLeafNodes()
    {
        return leafNodes;
    }


    public String getNodeName()
    {
        if (!doesParentNodeExist())
        {
            return path.getFileName().toString();
        }
        return String.join(".",
                           getParentNode().getNodeName(),
                           path.getFileName().toString());
    }


    private boolean doesParentNodeExist()
    {
        return getParentNode() != null &&
               !getParentNode().getPath().normalize().toString().isEmpty();
    }


    public NodeType getNodeType()
    {
        return NodeType.PACKAGE;
    }

}
