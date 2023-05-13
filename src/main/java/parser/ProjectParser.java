package parser;

import model.tree.node.LeafNode;
import model.tree.node.PackageNode;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


/** This class is responsible for the parsing of a Java project. While parsing the project
 * it creates a tree where nodes are the packages and leafs are the Java source files.
 * In order to create the tree it uses the ASTNode API from the JDT library
 */
public class ProjectParser implements Parser {
	private final ProjectParserHelper projectParserHelper;
	private final Map<Path, PackageNode> packageNodes;

	public ProjectParser(ParserType parserType) {
		ParserFactory parserFactory = new ParserFactory(parserType);
		projectParserHelper = parserFactory.createParser();
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
		projectParserHelper.createRelationshipIdentifier(packageNodes);
		return rootPackageNode;
	}

	private void parseFolder(PackageNode currentNode) {
		try (DirectoryStream<Path> filesStream = Files.newDirectoryStream(currentNode.getNodesPath())) {
			for (Path path: filesStream) {
				if (Files.isDirectory(path)) {
					createPackageSubNode(currentNode, new PackageNode(getSubNodesPath(currentNode, path.toFile())));
				} else if (isExtensionJava(path.normalize().toString())) {
					createLeafNode(currentNode, projectParserHelper.createLeafNode(path), path.toFile());
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
		FileVisitor fileVisitor = projectParserHelper.createFileVisitor();
		fileVisitor.createAST(file, leafNode);
		currentNode.addLeafNode(leafNode);
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
