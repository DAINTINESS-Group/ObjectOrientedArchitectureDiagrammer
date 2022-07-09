package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.junit.jupiter.api.Test;

import model.PackageNode;
import model.LeafNode;

class ASTInheritanceTest {
	private Parser parser;
	private Map<String, PackageNode> packages;
	private LeafNode implementingClassLeaf;
	@Test
	void test() throws IOException, MalformedTreeException, BadLocationException, ParseException{
		parser = new Parser("src\\test\\resources\\InheritanceTesting\\src");
		packages = parser.getPackageNodes();
		PackageNode sourcePackage = packages.get("src");
		implementingClassLeaf = sourcePackage.getLeafNodes().get("ImplementingClass");
		assertEquals("ImplementingClass", implementingClassLeaf.getLeafBranches().get(0).getStartingLeafNode().getName());
		assertEquals("TestingInterface2", implementingClassLeaf.getLeafBranches().get(0).getEndingLeafNode().getName());
		assertEquals("implementation", implementingClassLeaf.getLeafBranches().get(0).getBranchType());
		
		assertEquals("ImplementingClass", implementingClassLeaf.getLeafBranches().get(1).getStartingLeafNode().getName());
		assertEquals("ExtensionClass", implementingClassLeaf.getLeafBranches().get(1).getEndingLeafNode().getName());
		assertEquals("extension", implementingClassLeaf.getLeafBranches().get(1).getBranchType());
		
		assertEquals("ImplementingClass", implementingClassLeaf.getLeafBranches().get(2).getStartingLeafNode().getName());
		assertEquals("TestingInterface", implementingClassLeaf.getLeafBranches().get(2).getEndingLeafNode().getName());
		assertEquals("implementation", implementingClassLeaf.getLeafBranches().get(2).getBranchType());
	}
		
}

