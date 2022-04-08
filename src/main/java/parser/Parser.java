package parser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import model.LeafNode;
import model.PackageNode;
import model.RecursiveFileVisitor;

public class Parser {
	private List<PackageNode> packageNodes;
	private PackageNode subNode;
	private PackageNode rootPackageNode;
	
	public Parser(String sourcePath) {
		packageNodes = new ArrayList<PackageNode>();
		rootPackageNode = new PackageNode(sourcePath);
		packageNodes.add(rootPackageNode);
		try {
			parseFolder(rootPackageNode);
		}catch (ParseException e) {
			e.printStackTrace();
		};
		File [] root = new File[1];
		root[0] = new File("src\\test\\resources\\LatexEditor\\src");
		RecursiveFileVisitor visitor = new RecursiveFileVisitor (); 
		try {
			visitor.visitAllFiles(root);
		}catch (MalformedTreeException m) {
			m.printStackTrace();
		}catch (IOException i ) {
			i.printStackTrace();
		}catch (BadLocationException b) {
			b.printStackTrace();
		}
	}

	public void parseFolder(PackageNode currentNode) throws ParseException{
		File folder = new File(currentNode.getNodesPath());
		for (File file: folder.listFiles()) {
			if (!file.isDirectory()) {
				if (isExtensionJava(file.getPath())) {
					currentNode.setValid();
					LeafNode leafNode = new LeafNode(file.getPath());
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
