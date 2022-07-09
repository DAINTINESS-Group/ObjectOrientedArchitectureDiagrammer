package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.junit.jupiter.api.Test;

import model.LeafNode;
import model.PackageNode;

class NodeTypeTest {
	private Parser parser;
	private Map<String, PackageNode> packages;
	@Test
	void test() throws IOException, MalformedTreeException, BadLocationException, ParseException{
		parser = new Parser("src\\test\\resources\\InheritanceTesting\\src");
		packages = parser.getPackageNodes();
		PackageNode sourcePackage = packages.get("src");
		List<LeafNode> classLeafs = new ArrayList<LeafNode>();
		List<LeafNode> interfaceLeafs = new ArrayList<LeafNode>();
		classLeafs.add(sourcePackage.getLeafNodes().get("ImplementingClass"));
		classLeafs.add(sourcePackage.getLeafNodes().get("ExtensionClass"));
		interfaceLeafs.add(sourcePackage.getLeafNodes().get("TestingInterface"));
		interfaceLeafs.add(sourcePackage.getLeafNodes().get("TestingInterface2"));
		for (LeafNode l: classLeafs) {
			assertEquals("class", l.getType());
		}
		for (LeafNode l: interfaceLeafs) {
			assertEquals("interface", l.getType());
		}
	}

}
