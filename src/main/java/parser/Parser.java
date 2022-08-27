package parser;

import model.tree.PackageNode;

import java.nio.file.Path;
import java.util.Map;

public interface Parser {

    /**
     * This method creates the root of the tree, from the path of the source package, calls the
     * parseFolder method, that's responsible for the parsing of the source's folder and creates an object
     * of the RelationshipIdentifier class with the created nodes in order to create the Relationships
     * @param sourcePackagePath the path of the project's source folder
     * @return the root of the tree
     */
    PackageNode parseSourcePackage(Path sourcePackagePath);

    Map<Path, PackageNode> getPackageNodes();
}
