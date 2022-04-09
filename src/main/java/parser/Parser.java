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
	private RecursiveFileVisitor fileVisitor;
	private List<PackageNode> packageNodes;
	private PackageNode subNode;
	private PackageNode rootPackageNode;
	
	public Parser(String sourcePath) throws IOException, MalformedTreeException, BadLocationException, ParseException{
		fileVisitor = new RecursiveFileVisitor ();
		packageNodes = new ArrayList<PackageNode>();
		rootPackageNode = new PackageNode(sourcePath);
		packageNodes.add(rootPackageNode);
		parseFolder(rootPackageNode);
	}

	public void parseFolder(PackageNode currentNode) throws ParseException, IOException, MalformedTreeException, BadLocationException{
		File folder = new File(currentNode.getNodesPath());
		for (File file: folder.listFiles()) {
			if (!file.isDirectory()) {
				if (isExtensionJava(file.getPath())) {
					currentNode.setValid();
					LeafNode leafNode = new LeafNode(file.getPath());
					currentNode.addLeafNode(leafNode);
					leafNode.setParrentNode(currentNode);
					fileVisitor.processJavaFile(file, leafNode);
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
		return filePath.toLowerCase().endsWith(".java");
	}
	
	private String getSubNodesPath(PackageNode currentPackage, File file) {
		return currentPackage.getNodesPath() + "\\" + file.getName();
	}
	
	public List<PackageNode> getPackageNodes() {
		return packageNodes;
	}
	
}
