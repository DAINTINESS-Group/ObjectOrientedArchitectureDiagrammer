package parser;

import parser.factory.Parser;
import parser.tree.LeafNode;
import parser.tree.PackageNode;
import parser.tree.Relationship;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is responsible for the creation of the branches between the Java
 * source files. The branches have a type, e.g., inheritance, implementation.
 * The branches are also directed with a starting and an ending node.
 */
public interface IRelationshipIdentifier {

	/**
	 * This method is responsible for creating the relationships of the package and leaf nodes.
	 *
	 * @param packageNodes The collection of package nodes that have been parsed.
	 * @return			   The collection of relationships that were created.
	 */
	Map<LeafNode, Set<Relationship<LeafNode>>> createLeafNodesRelationships(Map<Path, PackageNode> packageNodes);

	/**
	 * This method identifies the package node relationships by parsing the created leaf node relationships.
	 *
	 * @param  leafNodeRelationships The relationships that were created by {@link Parser#createRelationships(Map)}.
	 * @return 						 The package node relationships that were created by parsing the leaf node relationships.
	 */
	Map<PackageNode, Set<Relationship<PackageNode>>> identifyPackageNodeRelationships(Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships);
}
