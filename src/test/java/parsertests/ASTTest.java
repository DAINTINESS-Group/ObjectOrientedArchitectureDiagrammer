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

class ASTTest {
	private Parser parser;
	private List<PackageNode> packages;
	private List<String> methodReturnTypes;
	private List<String> fieldTypes;
	private List<String> methodParameterTypes;
	@Test
	void test() throws IOException, MalformedTreeException, BadLocationException, ParseException{
		//TODO add tests for branches
		parser = new Parser("src\\test\\resources\\LatexEditor\\src\\controller\\commands");
		packages = parser.getPackageNodes();
		methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
		fieldTypes = new ArrayList<>(Arrays.asList("VersionsManager"));
		methodParameterTypes = new ArrayList<>(Arrays.asList("VersionsManager"));
		for (PackageNode p: packages) {
			if ( p.getName().equals("commands") ) {
				p.getLeafNodes();
				for (LeafNode l: p.getLeafNodes()) {
					if ( l.getName().equals("AddLatexCommand") ) {
						List<String> methodReturnTypesTest = new ArrayList<>();
						List<String> fieldTypesTest = new ArrayList<>();
						List<String> methodParameterTypesTest = new ArrayList<String>();
						methodParameterTypesTest = l.getMethodParameterTypes();
						fieldTypesTest = l.getFieldTypes();
						methodReturnTypesTest = l.getMethodReturnTypes();
						Collections.sort(methodReturnTypesTest);
						Collections.sort(methodReturnTypes);
						assertTrue(methodReturnTypesTest.size() == methodReturnTypes.size() 
								&& methodReturnTypes.containsAll(methodReturnTypesTest) 
								&& methodReturnTypesTest.containsAll(methodReturnTypes));
						Collections.sort(fieldTypesTest);
						Collections.sort(fieldTypes);
						assertTrue(fieldTypesTest.size() == fieldTypes.size() 
								&& fieldTypes.containsAll(fieldTypesTest) 
								&& fieldTypesTest.containsAll(fieldTypes));
						Collections.sort(methodParameterTypesTest);
						Collections.sort(methodParameterTypes);
						assertTrue(methodParameterTypesTest.size() == methodParameterTypes.size() 
								&& methodParameterTypes.containsAll(methodParameterTypesTest) 
								&& methodParameterTypes.containsAll(methodParameterTypesTest));
					}
					if ( l.getName().equals("CommandFactory") ) {
						assertEquals("CommandFactory", l.getLeafBranches().get(0).getStartingLeafNode().getName(), "message");
						assertEquals("Command", l.getLeafBranches().get(0).getEndingLeafNode().getName(), "message");
					}
				}
			}
		}
	}
		
}

