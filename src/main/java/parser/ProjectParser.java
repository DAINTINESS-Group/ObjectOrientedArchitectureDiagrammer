package parser;

import parser.factory.Parser;
import parser.tree.LeafNode;
import parser.tree.PackageNode;
import parser.tree.Relationship;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProjectParser implements Parser {

	private final Map<Path, PackageNode> packageNodes;

	public ProjectParser() {
		this.packageNodes = new HashMap<>();
	}


	@Override
	public Map<Path, PackageNode> parseSourcePackage(Path sourcePackagePath) {
		PackageNode sourcePackage = new PackageNode(sourcePackagePath);
		packageNodes.put(sourcePackage.getPath(), sourcePackage);
		parseFolder(sourcePackage);
		return packageNodes;
	}

	@Override
	public Map<LeafNode, Set<Relationship<LeafNode>>> createRelationships(Map<Path, PackageNode> packageNodes) {
		IRelationshipIdentifier relationshipIdentifier = new RelationshipIdentifier();
		return relationshipIdentifier.createLeafNodesRelationships(packageNodes);
	}

	@Override
	public Map<PackageNode, Set<Relationship<PackageNode>>> identifyPackageNodeRelationships(Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships) {
		IRelationshipIdentifier relationshipIdentifier = new RelationshipIdentifier();
		return relationshipIdentifier.identifyPackageNodeRelationships(leafNodeRelationships);
	}

	private void parseFolder(PackageNode currentNode) {
		try (DirectoryStream<Path> filesStream = Files.newDirectoryStream(currentNode.getPath())) {
			for (Path path: filesStream) {
				if (Files.isDirectory(path)) {
					PackageNode subNode = new PackageNode(currentNode, path);
					packageNodes.put(subNode.getPath(), subNode);
					currentNode.getSubNodes().put(subNode.getPath(), subNode);
					parseFolder(subNode);
				}
				else if (isJavaSourceFile(path)) {
					FileVisitor fileVisitor = new FileVisitor(currentNode, path);
					LeafNode    leafNode    = fileVisitor.createAST();
					currentNode.getLeafNodes().put(leafNode.nodeName(), leafNode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	private boolean isJavaSourceFile(Path filePath) {
		return filePath.normalize().toString().toLowerCase().endsWith(".java");
	}

}
