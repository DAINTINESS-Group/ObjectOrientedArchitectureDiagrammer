package parser.jdt;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.LeafNode;
import parser.tree.NodeType;
import parser.tree.PackageNode;
import parser.tree.Relationship;
import parser.tree.RelationshipType;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
class JDTRelationshipIdentifierTest {

	ParserType parserType = ParserType.JDT;

	@Deprecated
	@Test
	void getFieldAndMethodTypesTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		List<String> methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
		List<String> fieldTypes = new ArrayList<>(List.of("VersionsManager"));
		List<String> methodParameterTypes = new ArrayList<>(List.of("VersionsManager"));

		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
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
	}

	@Deprecated
	@Test
	void leafNodeRelationshipsTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));

		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");
		List<Relationship<LeafNode>> nodeRelationships = addLatexCommand.getLeafNodeRelationships();

		boolean foundObligatoryRelationship = false;
		int relationshipCounter = 0;
		for (Relationship<LeafNode> relationship : nodeRelationships) {
			if ((relationship.startingNode().getName().equals("AddLatexCommand")) && (relationship.endingNode().getName().equals("VersionsManager"))) {
				if (relationship.relationshipType().equals(RelationshipType.DEPENDENCY)) {
					foundObligatoryRelationship = true;
				} else {
					foundObligatoryRelationship = relationship.relationshipType().equals(RelationshipType.ASSOCIATION);
				}
			} else if ((relationship.startingNode().getName().equals("AddLatexCommand")) && (relationship.endingNode().getName().equals("Command"))) {
				assertEquals(RelationshipType.IMPLEMENTATION, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else {
				foundObligatoryRelationship = false;
			}
			relationshipCounter++;
		}
		assertEquals(3, relationshipCounter);
		assertTrue(foundObligatoryRelationship);
		assertEquals(NodeType.CLASS, addLatexCommand.getType());

		LeafNode commandFactory = commandPackage.getLeafNodes().get("CommandFactory");
		nodeRelationships = commandFactory.getLeafNodeRelationships();

		boolean foundObligatoryRelationships_CommandFactoryToVersionsManager = false;
		boolean foundObligatoryRelationships_CommandFactoryToCommand = false;
		relationshipCounter = 0;
		for (Relationship<LeafNode> relationship : nodeRelationships) {
			if ((relationship.startingNode().getName().equals("CommandFactory")) && (relationship.endingNode().getName().equals("VersionsManager"))) {
				if (relationship.relationshipType().equals(RelationshipType.DEPENDENCY) || (relationship.relationshipType().equals(RelationshipType.ASSOCIATION))) {
					foundObligatoryRelationships_CommandFactoryToVersionsManager = true;
				}
			} else if ((relationship.startingNode().getName().equals("CommandFactory")) && (relationship.endingNode().getName().equals("Command"))) {
				if (relationship.relationshipType().equals(RelationshipType.DEPENDENCY))
					foundObligatoryRelationships_CommandFactoryToCommand = true;
			}
			relationshipCounter++;
		}
		assertTrue(foundObligatoryRelationships_CommandFactoryToVersionsManager);
		assertTrue(foundObligatoryRelationships_CommandFactoryToCommand);
		assertEquals(4, relationshipCounter);
		assertEquals(NodeType.CLASS, commandFactory.getType());
	}

	@Deprecated
	@Test
	void leafNodeInheritanceRelationshipTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "ParserTesting")));
		PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "ParserTesting")));

		LeafNode implementingClassLeaf = sourcePackage.getLeafNodes().get("ImplementingClass");
		List<Relationship<LeafNode>> nodeRelationships = implementingClassLeaf.getLeafNodeRelationships();

		boolean foundObligatoryRelationship = false;
		int relationshipCounter = 0;
		for (Relationship<LeafNode> relationship : nodeRelationships) {
			if ((relationship.startingNode().getName().equals("ImplementingClass")) && (relationship.endingNode().getName().equals("TestingInterface2"))) {
				assertEquals(RelationshipType.IMPLEMENTATION, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else if ((relationship.startingNode().getName().equals("ImplementingClass")) && (relationship.endingNode().getName().equals("ExtensionClass"))) {
				assertEquals(RelationshipType.EXTENSION, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else if ((relationship.startingNode().getName().equals("ImplementingClass")) && (relationship.endingNode().getName().equals("TestingInterface"))) {
				assertEquals(RelationshipType.IMPLEMENTATION, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else {
				foundObligatoryRelationship = false;
			}
			relationshipCounter++;
		}

		assertTrue(foundObligatoryRelationship);
		assertEquals(3, relationshipCounter);
		assertEquals(NodeType.CLASS, implementingClassLeaf.getType());
	}

	@Deprecated
	@Test
	
	void packageNodeRelationshipsTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);
		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));

		PackageNode commands = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		List<Relationship<PackageNode>> packageRelationships = commands.getPackageNodeRelationships();

		boolean foundObligatoryRelationship = false;
		int relationshipCounter = 0;
		for (Relationship<PackageNode> relationship : packageRelationships) {
			if ((relationship.startingNode().getName().equals("src.controller.commands")) && (relationship.endingNode().getName().equals("src.model"))) {
				assertEquals(RelationshipType.DEPENDENCY, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else {
				foundObligatoryRelationship = false;
			}
			relationshipCounter++;
		}

		assertTrue(foundObligatoryRelationship);
		assertEquals(1, relationshipCounter);
		assertEquals(NodeType.PACKAGE, commands.getType());

		PackageNode controller = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		packageRelationships = controller.getPackageNodeRelationships();

		foundObligatoryRelationship = false;
		relationshipCounter = 0;
		for (Relationship<PackageNode> relationship : packageRelationships) {
			if ((relationship.startingNode().getName().equals("src.controller")) && (relationship.endingNode().getName().equals("src.model"))) {
				assertEquals(RelationshipType.DEPENDENCY, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else if ((relationship.startingNode().getName().equals("src.controller")) && (relationship.endingNode().getName().equals("src.controller.commands"))) {
				assertEquals(RelationshipType.DEPENDENCY, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else {
				foundObligatoryRelationship = false;
			}
			relationshipCounter++;
		}

		assertTrue(foundObligatoryRelationship);
		assertEquals(2, relationshipCounter);
		assertEquals(NodeType.PACKAGE, commands.getType());
	}

	@Deprecated
	@Test
	void leafNodeTypesTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);
		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "ParserTesting")));
		PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "ParserTesting")));

		List<LeafNode> classLeafs = new ArrayList<>();
		List<LeafNode> interfaceLeafs = new ArrayList<>();
		classLeafs.add(sourcePackage.getLeafNodes().get("ImplementingClass"));
		classLeafs.add(sourcePackage.getLeafNodes().get("ExtensionClass"));
		interfaceLeafs.add(sourcePackage.getLeafNodes().get("TestingInterface"));
		interfaceLeafs.add(sourcePackage.getLeafNodes().get("TestingInterface2"));
		for (LeafNode l : classLeafs) {
			assertEquals(NodeType.CLASS, l.getType());
		}
		for (LeafNode l : interfaceLeafs) {
			assertEquals(NodeType.INTERFACE, l.getType());
		}
	}
}

