package parser;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import model.LeafNode;
import model.PackageNode;

public class Parser {
	private List<PackageNode> packageNodes;
	private PackageNode subNode;
	private PackageNode rootPackageNode;
	
	public Parser(String sourcePath) {
		packageNodes = new ArrayList<PackageNode>();
		rootPackageNode = new PackageNode(sourcePath);
		try {
			parseFolder(rootPackageNode);
		}catch (ParseException e) {
			e.printStackTrace();
		};
	}

	public void parseFolder(PackageNode currentNode) throws ParseException{
		File folder = new File(currentNode.getNodesPath());
		for (File file: folder.listFiles()) {
			if (!file.isDirectory()) {
				if (isExtensionJava(file.getAbsolutePath())) {
					currentNode.setValid();
					LeafNode leafNode = new LeafNode(file.getAbsolutePath());
					currentNode.addLeafNode(leafNode);
					leafNode.setParrentNode(currentNode);
				}
			}
			else {
				subNode = new PackageNode(getSubNodesPath(currentNode, file));
				packageNodes.add(subNode);
				currentNode.addSubNode(subNode);
				subNode.setParentNode(currentNode);
				parseFolder(subNode);
			}
		}
	}

	private boolean isExtensionJava(String filePath) {
		return getFilesExtension(filePath).equals(".java");
	}

	private String getFilesExtension(String filePath) {
		return filePath.substring(filePath.lastIndexOf("\\"))
				.substring(filePath.substring(filePath.lastIndexOf("\\")).indexOf("."));
	}
	
	private String getSubNodesPath(PackageNode currentPackage, File file) {
		return currentPackage.getNodesPath() + "\\" + file.getName();
	}
	
	public List<PackageNode> getPackageNodes() {
		return packageNodes;
	}
}
