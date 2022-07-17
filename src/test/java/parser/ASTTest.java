package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import model.LeafNodeType;
import model.RelationshipType;
import org.junit.jupiter.api.Test;
import model.PackageNode;
import model.LeafNode;

class ASTTest {

	@Test
	void test() {
		PackageParser parser = new Parser();
		parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
		Map<String, PackageNode> packages = parser.getPackageNodes();
		List<String> methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
		List<String> fieldTypes = new ArrayList<>(List.of("VersionsManager"));
		List<String> methodParameterTypes = new ArrayList<>(List.of("VersionsManager"));
		PackageNode commandPackage = packages.get("commands");

		LeafNode addLatexCommandLeaf = commandPackage.getLeafNodes().get("AddLatexCommand");
		List<String> methodReturnTypesTest;
		List<String> fieldTypesTest;
		List<String> methodParameterTypesTest;
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
				&& methodParameterTypesTest.containsAll(methodParameterTypes));
		
		assertEquals("AddLatexCommand", addLatexCommandLeaf.getLeafNodeRelationships().get(0).getStartingLeafNode().getName());
		assertEquals("VersionsManager", addLatexCommandLeaf.getLeafNodeRelationships().get(0).getEndingLeafNode().getName());
		assertEquals(RelationshipType.DEPENDENCY, addLatexCommandLeaf.getLeafNodeRelationships().get(0).getRelationshipType());
		assertEquals("AddLatexCommand", addLatexCommandLeaf.getLeafNodeRelationships().get(1).getStartingLeafNode().getName());
		assertEquals("Command", addLatexCommandLeaf.getLeafNodeRelationships().get(1).getEndingLeafNode().getName());
		assertEquals(RelationshipType.IMPLEMENTATION, addLatexCommandLeaf.getLeafNodeRelationships().get(1).getRelationshipType());
		assertEquals(LeafNodeType.CLASS, addLatexCommandLeaf.getType());

		LeafNode commandFactoryLeaf = commandPackage.getLeafNodes().get("CommandFactory");
		assertEquals("CommandFactory", commandFactoryLeaf.getLeafNodeRelationships().get(0).getStartingLeafNode().getName(), "message");
		assertEquals("VersionsManager", commandFactoryLeaf.getLeafNodeRelationships().get(0).getEndingLeafNode().getName(), "message");
		assertEquals(RelationshipType.DEPENDENCY, commandFactoryLeaf.getLeafNodeRelationships().get(0).getRelationshipType(), "message");
		assertEquals("DocumentManager", commandFactoryLeaf.getLeafNodeRelationships().get(1).getEndingLeafNode().getName(), "message");
		assertEquals(RelationshipType.ASSOCIATION, commandFactoryLeaf.getLeafNodeRelationships().get(1).getRelationshipType(), "message");
		assertEquals("Command", commandFactoryLeaf.getLeafNodeRelationships().get(2).getEndingLeafNode().getName(), "message");
		assertEquals(RelationshipType.DEPENDENCY, commandFactoryLeaf.getLeafNodeRelationships().get(2).getRelationshipType(), "message");
	}
		
}

