package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import model.tree.NodeType;
import model.tree.RelationshipType;
import org.junit.jupiter.api.Test;
import model.tree.PackageNode;
import model.tree.LeafNode;

class TreeStructureArchitectureTest {

	@Test
	void getFieldAndMethodTypesTest() {
		PackageParser parser = new Parser();
		parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
		Map<String, PackageNode> packages = parser.getPackageNodes();
		List<String> methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
		List<String> fieldTypes = new ArrayList<>(List.of("VersionsManager"));
		List<String> methodParameterTypes = new ArrayList<>(List.of("VersionsManager"));

		PackageNode commandPackage = packages.get("commands");
		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

		List<String> methodReturnTypesTest;
		List<String> fieldTypesTest;
		List<String> methodParameterTypesTest;
		methodParameterTypesTest = addLatexCommand.getMethodParameterTypes();
		fieldTypesTest = addLatexCommand.getFieldsTypes();
		methodReturnTypesTest = addLatexCommand.getMethodsReturnTypes();
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
		
		assertEquals("AddLatexCommand", addLatexCommand.getNodeRelationships().get(0).getStartingNode().getName());
		assertEquals("VersionsManager", addLatexCommand.getNodeRelationships().get(0).getEndingNode().getName());
		assertEquals(RelationshipType.DEPENDENCY, addLatexCommand.getNodeRelationships().get(0).getRelationshipType());
		assertEquals("AddLatexCommand", addLatexCommand.getNodeRelationships().get(1).getStartingNode().getName());
		assertEquals("Command", addLatexCommand.getNodeRelationships().get(1).getEndingNode().getName());
		assertEquals(RelationshipType.IMPLEMENTATION, addLatexCommand.getNodeRelationships().get(1).getRelationshipType());
		assertEquals(NodeType.CLASS, addLatexCommand.getType());

		LeafNode commandFactoryLeaf = commandPackage.getLeafNodes().get("CommandFactory");
		assertEquals("CommandFactory", commandFactoryLeaf.getNodeRelationships().get(0).getStartingNode().getName(), "message");
		assertEquals("VersionsManager", commandFactoryLeaf.getNodeRelationships().get(0).getEndingNode().getName(), "message");
		assertEquals(RelationshipType.DEPENDENCY, commandFactoryLeaf.getNodeRelationships().get(0).getRelationshipType(), "message");
		assertEquals("DocumentManager", commandFactoryLeaf.getNodeRelationships().get(1).getEndingNode().getName(), "message");
		assertEquals(RelationshipType.ASSOCIATION, commandFactoryLeaf.getNodeRelationships().get(1).getRelationshipType(), "message");
		assertEquals("Command", commandFactoryLeaf.getNodeRelationships().get(2).getEndingNode().getName(), "message");
		assertEquals(RelationshipType.DEPENDENCY, commandFactoryLeaf.getNodeRelationships().get(2).getRelationshipType(), "message");
	}

	@Test
	void leafNodeRelationshipsTest() {
		PackageParser parser = new Parser();
		parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
		Map<String, PackageNode> packages = parser.getPackageNodes();
		PackageNode commandPackage = packages.get("commands");

		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

		assertEquals("AddLatexCommand", addLatexCommand.getNodeRelationships().get(0).getStartingNode().getName());
		assertEquals("VersionsManager", addLatexCommand.getNodeRelationships().get(0).getEndingNode().getName());
		assertEquals(RelationshipType.DEPENDENCY, addLatexCommand.getNodeRelationships().get(0).getRelationshipType());
		assertEquals("AddLatexCommand", addLatexCommand.getNodeRelationships().get(1).getStartingNode().getName());
		assertEquals("Command", addLatexCommand.getNodeRelationships().get(1).getEndingNode().getName());
		assertEquals(RelationshipType.IMPLEMENTATION, addLatexCommand.getNodeRelationships().get(1).getRelationshipType());
		assertEquals(NodeType.CLASS, addLatexCommand.getType());

		LeafNode commandFactory = commandPackage.getLeafNodes().get("CommandFactory");

		assertEquals("CommandFactory", commandFactory.getNodeRelationships().get(0).getStartingNode().getName(), "message");
		assertEquals("VersionsManager", commandFactory.getNodeRelationships().get(0).getEndingNode().getName(), "message");
		assertEquals(RelationshipType.DEPENDENCY, commandFactory.getNodeRelationships().get(0).getRelationshipType(), "message");
		assertEquals("DocumentManager", commandFactory.getNodeRelationships().get(1).getEndingNode().getName(), "message");
		assertEquals(RelationshipType.ASSOCIATION, commandFactory.getNodeRelationships().get(1).getRelationshipType(), "message");
		assertEquals("Command", commandFactory.getNodeRelationships().get(2).getEndingNode().getName(), "message");
		assertEquals(RelationshipType.DEPENDENCY, commandFactory.getNodeRelationships().get(2).getRelationshipType(), "message");
	}

	@Test
	void leadNodeInheritanceRelationshipTest(){
		PackageParser parser = new Parser();
		parser.parseSourcePackage("src\\test\\resources\\InheritanceTesting\\src");
		Map<String, PackageNode> packages = parser.getPackageNodes();
		PackageNode sourcePackage = packages.get("src");
		LeafNode implementingClassLeaf = sourcePackage.getLeafNodes().get("ImplementingClass");
		assertEquals("ImplementingClass", implementingClassLeaf.getNodeRelationships().get(0).getStartingNode().getName());
		assertEquals("TestingInterface2", implementingClassLeaf.getNodeRelationships().get(0).getEndingNode().getName());
		assertEquals(RelationshipType.IMPLEMENTATION, implementingClassLeaf.getNodeRelationships().get(0).getRelationshipType());

		assertEquals("ImplementingClass", implementingClassLeaf.getNodeRelationships().get(1).getStartingNode().getName());
		assertEquals("ExtensionClass", implementingClassLeaf.getNodeRelationships().get(1).getEndingNode().getName());
		assertEquals(RelationshipType.EXTENSION, implementingClassLeaf.getNodeRelationships().get(1).getRelationshipType());

		assertEquals("ImplementingClass", implementingClassLeaf.getNodeRelationships().get(2).getStartingNode().getName());
		assertEquals("TestingInterface", implementingClassLeaf.getNodeRelationships().get(2).getEndingNode().getName());
		assertEquals(RelationshipType.IMPLEMENTATION, implementingClassLeaf.getNodeRelationships().get(2).getRelationshipType());
	}

	@Test
	void packageNodeRelationshipsTest(){
		PackageParser parser = new Parser();
		parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
		Map<String, PackageNode> packages = parser.getPackageNodes();
		PackageNode commands = packages.get("commands");

		assertEquals("commands", commands.getNodeRelationships().get(0).getStartingNode().getName());
		assertEquals("model", commands.getNodeRelationships().get(0).getEndingNode().getName());
		assertEquals(RelationshipType.DEPENDENCY, commands.getNodeRelationships().get(0).getRelationshipType());

		PackageNode controller = packages.get("controller");

		assertEquals("controller", controller.getNodeRelationships().get(0).getStartingNode().getName());
		assertEquals("model", controller.getNodeRelationships().get(0).getEndingNode().getName());
		assertEquals(RelationshipType.DEPENDENCY, controller.getNodeRelationships().get(0).getRelationshipType());
	}

	@Test
	void leafNodeTypesTest(){
		PackageParser parser = new Parser();
		parser.parseSourcePackage("src\\test\\resources\\InheritanceTesting\\src");
		Map<String, PackageNode> packages = parser.getPackageNodes();
		PackageNode sourcePackage = packages.get("src");
		List<LeafNode> classLeafs = new ArrayList<>();
		List<LeafNode> interfaceLeafs = new ArrayList<>();
		classLeafs.add(sourcePackage.getLeafNodes().get("ImplementingClass"));
		classLeafs.add(sourcePackage.getLeafNodes().get("ExtensionClass"));
		interfaceLeafs.add(sourcePackage.getLeafNodes().get("TestingInterface"));
		interfaceLeafs.add(sourcePackage.getLeafNodes().get("TestingInterface2"));
		for (LeafNode l: classLeafs) {
			assertEquals(NodeType.CLASS, l.getType());
		}
		for (LeafNode l: interfaceLeafs) {
			assertEquals(NodeType.INTERFACE, l.getType());
		}
	}

}

