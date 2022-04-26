package parsertests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.junit.jupiter.api.Test;

import parser.Parser;
import model.PackageNode;
import model.LeafBranch;
import model.LeafNode;

class ASTInheritanceTest {
	private Parser parser;
	private List<PackageNode> packages;
	@Test
	void test() throws IOException, MalformedTreeException, BadLocationException, ParseException{
		parser = new Parser("src\\test\\resources\\InheritanceTesting\\src");
		packages = parser.getPackageNodes();
		for (PackageNode p: packages) {
			if ( p.getName().equals("src") ) {
				for (LeafNode l: p.getLeafNodes()) {
					if ( l.getName().equals("ImplementingClass") ) {
						assertEquals("ImplementingClass", l.getLeafBranches().get(0).getStartingLeafNode().getName());
						assertEquals("ExtensionClass", l.getLeafBranches().get(0).getEndingLeafNode().getName());
						assertEquals("extension", l.getLeafBranches().get(0).getBranchType());
						
						assertEquals("ImplementingClass", l.getLeafBranches().get(1).getStartingLeafNode().getName());
						assertEquals("TestingInterface", l.getLeafBranches().get(1).getEndingLeafNode().getName());
						assertEquals("implementation", l.getLeafBranches().get(1).getBranchType());
						
						assertEquals("ImplementingClass", l.getLeafBranches().get(2).getStartingLeafNode().getName());
						assertEquals("TestingInterface2", l.getLeafBranches().get(2).getEndingLeafNode().getName());
						assertEquals("implementation", l.getLeafBranches().get(2).getBranchType());
					}
				}
			}
		}
	}
		
}

