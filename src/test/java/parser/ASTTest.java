package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.junit.jupiter.api.Test;

import model.PackageNode;
import model.LeafNode;

class ASTTest {
	private Parser parser;
	private Map<String, PackageNode> packages;
	private List<String> methodReturnTypes;
	private List<String> fieldTypes;
	private List<String> methodParameterTypes;
	private PackageNode commandPackage;
	private LeafNode addLatexCommandLeaf;
	private LeafNode commandFactoryLeaf;
	@Test
	void test() throws IOException, MalformedTreeException, BadLocationException, ParseException{
		parser = new Parser("src\\test\\resources\\LatexEditor\\src");
		packages = parser.getPackageNodes();
		methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
		fieldTypes = new ArrayList<>(Arrays.asList("VersionsManager"));
		methodParameterTypes = new ArrayList<>(Arrays.asList("VersionsManager"));
		commandPackage = packages.get("commands");
		
		addLatexCommandLeaf = commandPackage.getLeafNodes().get("AddLatexCommand");
		List<String> methodReturnTypesTest = new ArrayList<>();
		List<String> fieldTypesTest = new ArrayList<>();
		List<String> methodParameterTypesTest = new ArrayList<String>();
		methodParameterTypesTest = addLatexCommandLeaf.getMethodParameterTypes();
		fieldTypesTest = addLatexCommandLeaf.getFieldsTypes();
		methodReturnTypesTest = addLatexCommandLeaf.getMethodsReturnTypes();
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
		
		assertEquals("AddLatexCommand", addLatexCommandLeaf.getLeafBranches().get(0).getStartingLeafNode().getName());
		assertEquals("VersionsManager", addLatexCommandLeaf.getLeafBranches().get(0).getEndingLeafNode().getName());
		assertEquals("dependency", addLatexCommandLeaf.getLeafBranches().get(0).getBranchType());
		assertEquals("AddLatexCommand", addLatexCommandLeaf.getLeafBranches().get(1).getStartingLeafNode().getName());
		assertEquals("Command", addLatexCommandLeaf.getLeafBranches().get(1).getEndingLeafNode().getName());
		assertEquals("implementation", addLatexCommandLeaf.getLeafBranches().get(1).getBranchType());
		assertEquals("class", addLatexCommandLeaf.getType());
		
		commandFactoryLeaf = commandPackage.getLeafNodes().get("CommandFactory");
		assertEquals("CommandFactory", commandFactoryLeaf.getLeafBranches().get(0).getStartingLeafNode().getName(), "message");
		assertEquals("VersionsManager", commandFactoryLeaf.getLeafBranches().get(0).getEndingLeafNode().getName(), "message");
		assertEquals("dependency", commandFactoryLeaf.getLeafBranches().get(0).getBranchType(), "message");
		assertEquals("DocumentManager", commandFactoryLeaf.getLeafBranches().get(1).getEndingLeafNode().getName(), "message");
		assertEquals("association", commandFactoryLeaf.getLeafBranches().get(1).getBranchType(), "message");
		assertEquals("Command", commandFactoryLeaf.getLeafBranches().get(2).getEndingLeafNode().getName(), "message");
		assertEquals("dependency", commandFactoryLeaf.getLeafBranches().get(2).getBranchType(), "message");
	}
		
}

