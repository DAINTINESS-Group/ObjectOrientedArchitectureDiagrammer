package parser;

import java.nio.*;
import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.tree.LeafNode;
import model.tree.RelationshipIdentifier;
import model.tree.PackageNode;


/** This class is responsible for the parsing of a Java project. While parsing the project
 * it creates a tree where nodes are the packages and leafs are the Java source files.
 * In order to create the tree it uses the ASTNode API from the JDT library
 */
public class ProjectParser implements Parser {
	private final Map<String, PackageNode> packageNodes;

	public ProjectParser() {
		packageNodes = new HashMap<>();
	}

	public PackageNode parseSourcePackage(String sourcePackagePath) {
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

	private void parseFolder(PackageNode currentNode) throws ParseException{
		File folder = new File(currentNode.getNodesPath());
		for (File file: Objects.requireNonNull(folder.listFiles())) {
			if (file.isDirectory()) {
				createPackageSubNode(currentNode, new PackageNode(getSubNodesPath(currentNode, file)));
			}
			else if (isExtensionJava(file.getPath())){
				createLeafNode(currentNode, new LeafNode(file.getPath()), file);
			}
		}
	}

	private void createPackageSubNode(PackageNode currentNode, PackageNode subNode) throws ParseException{
		subNode.setParentNode(currentNode);
		packageNodes.put(subNode.getNodesPath(), subNode);
		currentNode.addSubNode(subNode);
		parseFolder(subNode);
	}

	private void createLeafNode(PackageNode currentNode, LeafNode leafNode, File file) {
		leafNode.setParentNode(currentNode);
		currentNode.setValid();
		currentNode.addLeafNode(leafNode);
		new FileVisitor(file, leafNode, packageNodes);
	}
	
	private boolean isExtensionJava(String filePath) {
		return filePath.toLowerCase().endsWith(".java");
	}
	
	private String getSubNodesPath(PackageNode currentPackage, File file) {
		return currentPackage.getNodesPath() + "\\" + file.getName();
	}
	
	/** This method returns the map with keys the name of the package and values
	 * the object of type PackageNode
	 */
	public Map<String, PackageNode> getPackageNodes() {
		return packageNodes;
	}

}
