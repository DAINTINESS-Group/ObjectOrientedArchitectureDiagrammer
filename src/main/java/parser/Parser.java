package parser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import model.LeafBranch;
import model.LeafNode;
import model.LeafNodeRelation;
import model.PackageNode;
import model.RecursiveFileVisitor;

public class Parser {
	private RecursiveFileVisitor fileVisitor;
	private LeafNodeRelation leafNodeRelation;
	private List<PackageNode> packageNodes;
	private List<LeafBranch> leafBranches;
	private PackageNode rootPackageNode;
	
	public Parser(String sourcePath) throws IOException, MalformedTreeException, BadLocationException, ParseException{
		fileVisitor = new RecursiveFileVisitor ();
		packageNodes = new ArrayList<PackageNode>();
		rootPackageNode = new PackageNode(sourcePath);
		packageNodes.add(rootPackageNode);
		parseFolder(rootPackageNode);
		leafNodeRelation = new LeafNodeRelation(packageNodes);
		
	}

	private void parseFolder(PackageNode currentNode) throws ParseException, IOException, MalformedTreeException, BadLocationException{
		File folder = new File(currentNode.getNodesPath());
		for (File file: folder.listFiles()) {
			if (!file.isDirectory() && isExtensionJava(file.getPath())) {
				createLeafNode(currentNode, new LeafNode(file.getPath()), file);
			}
			else {
				createPackageSubNode(currentNode, new PackageNode(getSubNodesPath(currentNode, file)));
			}
		}
	}

	private void createPackageSubNode(PackageNode currentNode, PackageNode subNode) throws ParseException, IOException, BadLocationException {
		packageNodes.add(subNode);
		currentNode.addSubNode(subNode);
		subNode.setParentNode(currentNode);
		parseFolder(subNode);
	}

	private void createLeafNode(PackageNode currentNode, LeafNode leafNode, File file) throws IOException, BadLocationException {
		currentNode.setValid();
		currentNode.addLeafNode(leafNode);
		leafNode.setParrentNode(currentNode);
		fileVisitor.createAST(file, leafNode, packageNodes);
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
	 
	public List<LeafBranch> getLeafBranches() {
		return leafBranches;
	}
	
}
