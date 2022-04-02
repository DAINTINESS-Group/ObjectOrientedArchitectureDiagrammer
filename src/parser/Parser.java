package parser;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	public static void main(String[] args) throws ParseException{
		Parser parser = new Parser(args[0]);
	}
	
	private List<Package> packages;
	private Package childrenPackage;
	private Package sourcePackage;
	
	public Parser(String sourcePath) {
		packages = new ArrayList<Package>();
		sourcePackage = new Package(sourcePath);
		parseFolder(sourcePackage);
		printPackageTree();
	}

	public void parseFolder(Package currentPackage) {
		File folder = new File(currentPackage.getPath());
		for (File file: folder.listFiles()) {
			if (!file.isDirectory()) {
				if (getFilesExtension(file.getAbsolutePath()).equals(".java")) {
					currentPackage.setValid();
				}
			}
			else {
				childrenPackage = new Package(getChildrensPath(currentPackage, file));
				packages.add(childrenPackage);
				currentPackage.addChild(childrenPackage);
				childrenPackage.setParent(currentPackage);
				parseFolder(childrenPackage);
			}
		}
	}

	private String getChildrensPath(Package currentPackage, File file) {
		return currentPackage.getPath() + "\\" + file.getName();
	}

	private String getFilesExtension(String filePath) {
		return filePath.substring(filePath.lastIndexOf("\\"))
				.substring(filePath.substring(filePath.lastIndexOf("\\")).indexOf("."));
	}
	
	private void printPackageTree() {
		for (Package p: packages) {
			System.out.println("Name: " + p.getName());
			System.out.println("Path: " + p.getPath());
			System.out.println("Children: ");
			p.printChildren();
			System.out.println("Parent: " + p.getParent());
			System.out.println("Is Package valid: " + p.isValid());
		}
	}
}
