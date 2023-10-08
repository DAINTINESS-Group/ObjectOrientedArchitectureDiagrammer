package parser.javaparser;

import parser.factory.Parser;
import parser.tree.LeafNode;
import parser.tree.PackageNode;
import parser.tree.RelationshipIdentifier;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JavaparserProjectParser implements Parser {

	private final Map<Path, PackageNode> packageNodes;

	public JavaparserProjectParser() {
		packageNodes = new HashMap<>();
	}

	@Override
	public Map<Path, PackageNode> parseSourcePackage(Path sourcePackagePath) {
		PackageNode rootPackageNode = new PackageNode(sourcePackagePath);
		packageNodes.put(rootPackageNode.getPackageNodesPath(), rootPackageNode);
		try {
			parseFolder(rootPackageNode);
			RelationshipIdentifier relationshipIdentifier = new JavaparserRelationshipIdentifier(packageNodes);
			relationshipIdentifier.createLeafNodesRelationships();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packageNodes;
	}

	private void parseFolder(PackageNode currentNode) {
		try (DirectoryStream<Path> filesStream = Files.newDirectoryStream(currentNode.getPackageNodesPath())) {
			for (Path path: filesStream) {
				if (Files.isDirectory(path)) {
					createPackageSubNode(currentNode, new PackageNode(getSubNodesPath(currentNode, path.toFile())));
				} else if (isExtensionJava(path.normalize().toString())) {
					createLeafNode(currentNode, new JavaparserLeafNode(path), path.toFile());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createPackageSubNode(PackageNode currentNode, PackageNode subNode){
		subNode.setParentNode(currentNode);
		packageNodes.put(subNode.getPackageNodesPath(), subNode);
		currentNode.addSubNode(subNode);
		parseFolder(subNode);
	}

	private void createLeafNode(PackageNode currentNode, LeafNode leafNode, File file) {
		leafNode.setParentNode(currentNode);
		currentNode.setValid();
		JavaparserFileVisitor fileVisitor = new JavaparserFileVisitor();
		fileVisitor.createAST(file, leafNode);
		currentNode.addLeafNode(leafNode);
	}

	private boolean isExtensionJava(String filePath) {
		return filePath.toLowerCase().endsWith(".java");
	}

	private Path getSubNodesPath(PackageNode currentPackage, File file) {
		return Paths.get(currentPackage.getPackageNodesPath().normalize() + "/" + file.getName());
	}

}
