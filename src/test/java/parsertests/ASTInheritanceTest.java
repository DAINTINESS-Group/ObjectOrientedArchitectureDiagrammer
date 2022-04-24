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
						for (LeafBranch lb: l.getLeafBranches()) {
							System.out.println(lb.getStartingLeafNode().getName());
							System.out.println(lb.getEndingLeafNode().getName());
							System.out.println(lb.getBranchType());
							
						}
					}
				}
			}
		}
	}
		
}

