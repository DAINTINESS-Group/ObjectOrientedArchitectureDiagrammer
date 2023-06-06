package parser.jdt;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import parser.tree.node.NodeType;
import parser.tree.edge.RelationshipType;
import org.junit.jupiter.api.Test;
import parser.tree.node.PackageNode;
import parser.tree.edge.Relationship;
import parser.tree.node.LeafNode;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;

class JDTRelationshipIdentifierTest {

	Path currentDirectory = Path.of(".");
	ParserType parserType = ParserType.JDT;

	@Test
	void getFieldAndMethodTypesTest() {
		try {
			ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
			Parser parser = projectParserFactory.createProjectParser();

			parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			Map<Path, PackageNode> packages = parser.getPackageNodes();
			List<String> methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
			List<String> fieldTypes = new ArrayList<>(List.of("VersionsManager"));
			List<String> methodParameterTypes = new ArrayList<>(List.of("VersionsManager"));

			PackageNode commandPackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void leafNodeRelationshipsTest() {
		try {
			ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
			Parser parser = projectParserFactory.createProjectParser();

			parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			Map<Path, PackageNode> packages = parser.getPackageNodes();
			PackageNode commandPackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));

			LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");
			List<Relationship> nodeRelationships = addLatexCommand.getNodeRelationships();

			boolean foundObligatoryRelationship = false;
			int relationshipCounter = 0;
			for (Relationship relationship : nodeRelationships) {
				if ((relationship.getStartingNode().getName().equals("AddLatexCommand")) && (relationship.getEndingNode().getName().equals("VersionsManager"))) {
					if (relationship.getRelationshipType().equals(RelationshipType.DEPENDENCY)) {
						foundObligatoryRelationship = true;
					} else {
						foundObligatoryRelationship = relationship.getRelationshipType().equals(RelationshipType.ASSOCIATION);
					}
				} else if ((relationship.getStartingNode().getName().equals("AddLatexCommand")) && (relationship.getEndingNode().getName().equals("Command"))) {
					assertEquals(RelationshipType.IMPLEMENTATION, relationship.getRelationshipType());
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
			nodeRelationships = commandFactory.getNodeRelationships();

			boolean foundObligatoryRelationships_CommandFactoryToVersionsManager = false;
			boolean foundObligatoryRelationships_CommandFactoryToCommand = false;
			relationshipCounter = 0;
			for (Relationship relationship : nodeRelationships) {
				if ((relationship.getStartingNode().getName().equals("CommandFactory")) && (relationship.getEndingNode().getName().equals("VersionsManager"))) {
					if (relationship.getRelationshipType().equals(RelationshipType.DEPENDENCY) || (relationship.getRelationshipType().equals(RelationshipType.ASSOCIATION))) {
						foundObligatoryRelationships_CommandFactoryToVersionsManager = true;
					}
				} else if ((relationship.getStartingNode().getName().equals("CommandFactory")) && (relationship.getEndingNode().getName().equals("Command"))) {
					if (relationship.getRelationshipType().equals(RelationshipType.DEPENDENCY))
						foundObligatoryRelationships_CommandFactoryToCommand = true;
				}
				relationshipCounter++;
			}
			assertTrue(foundObligatoryRelationships_CommandFactoryToVersionsManager);
			assertTrue(foundObligatoryRelationships_CommandFactoryToCommand);
			assertEquals(4, relationshipCounter);
			assertEquals(NodeType.CLASS, commandFactory.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void leafNodeInheritanceRelationshipTest() {
		try {
			ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
			Parser parser = projectParserFactory.createProjectParser();

			parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\ParserTesting"));
			Map<Path, PackageNode> packages = parser.getPackageNodes();
			PackageNode sourcePackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\ParserTesting"));

			LeafNode implementingClassLeaf = sourcePackage.getLeafNodes().get("ImplementingClass");
			List<Relationship> nodeRelationships = implementingClassLeaf.getNodeRelationships();

			boolean foundObligatoryRelationship = false;
			int relationshipCounter = 0;
			for (Relationship relationship : nodeRelationships) {
				if ((relationship.getStartingNode().getName().equals("ImplementingClass")) && (relationship.getEndingNode().getName().equals("TestingInterface2"))) {
					assertEquals(RelationshipType.IMPLEMENTATION, relationship.getRelationshipType());
					foundObligatoryRelationship = true;
				} else if ((relationship.getStartingNode().getName().equals("ImplementingClass")) && (relationship.getEndingNode().getName().equals("ExtensionClass"))) {
					assertEquals(RelationshipType.EXTENSION, relationship.getRelationshipType());
					foundObligatoryRelationship = true;
				} else if ((relationship.getStartingNode().getName().equals("ImplementingClass")) && (relationship.getEndingNode().getName().equals("TestingInterface"))) {
					assertEquals(RelationshipType.IMPLEMENTATION, relationship.getRelationshipType());
					foundObligatoryRelationship = true;
				} else {
					foundObligatoryRelationship = false;
				}
				relationshipCounter++;
			}

			assertTrue(foundObligatoryRelationship);
			assertEquals(3, relationshipCounter);
			assertEquals(NodeType.CLASS, implementingClassLeaf.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void packageNodeRelationshipsTest() {
		try {
			ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
			Parser parser = projectParserFactory.createProjectParser();

			parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			Map<Path, PackageNode> packages = parser.getPackageNodes();

			PackageNode commands = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));
			List<Relationship> packageRelationships = commands.getNodeRelationships();

			boolean foundObligatoryRelationship = false;
			int relationshipCounter = 0;
			for (Relationship relationship : packageRelationships) {
				if ((relationship.getStartingNode().getName().equals("src.controller.commands")) && (relationship.getEndingNode().getName().equals("src.model"))) {
					assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
					foundObligatoryRelationship = true;
				} else {
					foundObligatoryRelationship = false;
				}
				relationshipCounter++;
			}

			assertTrue(foundObligatoryRelationship);
			assertEquals(1, relationshipCounter);
			assertEquals(NodeType.PACKAGE, commands.getType());

			PackageNode controller = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller"));
			packageRelationships = controller.getNodeRelationships();

			foundObligatoryRelationship = false;
			relationshipCounter = 0;
			for (Relationship relationship : packageRelationships) {
				if ((relationship.getStartingNode().getName().equals("src.controller")) && (relationship.getEndingNode().getName().equals("src.model"))) {
					assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
					foundObligatoryRelationship = true;
				} else if ((relationship.getStartingNode().getName().equals("src.controller")) && (relationship.getEndingNode().getName().equals("src.controller.commands"))) {
					assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
					foundObligatoryRelationship = true;
				} else {
					foundObligatoryRelationship = false;
				}
				relationshipCounter++;
			}

			assertTrue(foundObligatoryRelationship);
			assertEquals(2, relationshipCounter);
			assertEquals(NodeType.PACKAGE, commands.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void leafNodeTypesTest() {
		try {
			ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
			Parser parser = projectParserFactory.createProjectParser();

			parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\ParserTesting"));
			Map<Path, PackageNode> packages = parser.getPackageNodes();
			PackageNode sourcePackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\ParserTesting"));
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

