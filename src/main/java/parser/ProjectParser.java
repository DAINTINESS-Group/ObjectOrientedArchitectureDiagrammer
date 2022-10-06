package parser;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import model.tree.LeafNode;
import model.tree.RelationshipIdentifier;
import model.tree.PackageNode;


/** This class is responsible for the parsing of a Java project. While parsing the project
 * it creates a tree where nodes are the packages and leafs are the Java source files.
 * In order to create the tree it uses the ASTNode API from the JDT library
 */
public class ProjectParser implements Parser {
	private final Map<Path, PackageNode> packageNodes;

	public ProjectParser() {
		packageNodes = new HashMap<>();
	}

	public PackageNode parseSourcePackage(Path sourcePackagePath) {
		PackageNode rootPackageNode = new PackageNode(sourcePackagePath);
		packageNodes.put(rootPackageNode.getNodesPath(), rootPackageNode);
		try {
			parseFolder(rootPackageNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new RelationshipIdentifier(packageNodes);
		return rootPackageNode;
	}

	private void parseFolder(PackageNode currentNode) {
		try (DirectoryStream<Path> filesStream = Files.newDirectoryStream(currentNode.getNodesPath())) {
			for (Path path: filesStream) {
				if (Files.isDirectory(path)) {
					createPackageSubNode(currentNode, new PackageNode(getSubNodesPath(currentNode, path.toFile())));
				} else if (isExtensionJava(path.normalize().toString())) {
					createLeafNode(currentNode, new LeafNode(path), path.toFile());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createPackageSubNode(PackageNode currentNode, PackageNode subNode){
		subNode.setParentNode(currentNode);
		packageNodes.put(subNode.getNodesPath(), subNode);
		currentNode.addSubNode(subNode);
		parseFolder(subNode);
	}

	private void createLeafNode(PackageNode currentNode, LeafNode leafNode, File file) {
		leafNode.setParentNode(currentNode);
		currentNode.setValid();
		currentNode.addLeafNode(leafNode);
		new FileVisitor(file, leafNode);
	}
	
	private boolean isExtensionJava(String filePath) {
		return filePath.toLowerCase().endsWith(".java");
	}
	
	private Path getSubNodesPath(PackageNode currentPackage, File file) {
		return Paths.get(currentPackage.getNodesPath().normalize() + "\\" + file.getName());
	}
	
	/** This method returns the map with keys the name of the package and values
	 * the object of type PackageNode
	 */
	public Map<Path, PackageNode> getPackageNodes() {
		return packageNodes;
	}

}
