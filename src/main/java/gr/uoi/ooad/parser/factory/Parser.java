package gr.uoi.ooad.parser.factory;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import gr.uoi.ooad.parser.tree.LeafNode;
import gr.uoi.ooad.parser.tree.PackageNode;
import gr.uoi.ooad.parser.tree.Relationship;

public interface Parser {

    /**
     * This method creates the root of the tree, from the path of the source package, calls the
     * parseFolder method, that's responsible for the parsing of the source's folder and creates an
     * object of the RelationshipIdentifier class with the created nodes in order to create the
     * Relationships.
     *
     * @param sourcePackagePath the path of the project's source folder
     */
    Map<Path, PackageNode> parseSourcePackage(Path sourcePackagePath);

    /**
     * This method creates the relationships among the nodes of the tree
     *
     * @param packageNodes The collection of package nodes that have been parsed.
     * @return The collection of relationships that were created.
     */
    Map<LeafNode, Set<Relationship<LeafNode>>> createRelationships(
            Map<Path, PackageNode> packageNodes);

    /**
     * This method identifies the package node relationships by parsing the created leaf node
     * relationships.
     *
     * @param leafNodeRelationships The relationships that were created by {@link
     *     Parser#createRelationships(Map)}.
     * @return The package node relationships that were created by parsing the leaf node
     *     relationships.
     */
    Map<PackageNode, Set<Relationship<PackageNode>>> identifyPackageNodeRelationships(
            Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships);
}
